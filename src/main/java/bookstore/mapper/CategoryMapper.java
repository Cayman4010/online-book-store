package bookstore.mapper;

import bookstore.dto.book.CreateBookRequestDto;
import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CreateCategoryRequestDto;
import bookstore.model.Book;
import bookstore.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toCategory(CategoryDto categoryDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategory(CreateCategoryRequestDto requestDto, @MappingTarget Category category);
}
