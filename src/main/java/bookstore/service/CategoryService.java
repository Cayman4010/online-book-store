package bookstore.service;

import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);
    CategoryDto getById(Long id);
    CategoryDto save(CategoryDto categoryDto);
    CategoryDto updateById(Long id, CreateCategoryRequestDto requestDto);
    void deleteById(Long id);
}
