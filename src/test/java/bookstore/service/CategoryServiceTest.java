package bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CreateCategoryRequestDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CategoryMapper;
import bookstore.model.Category;
import bookstore.repository.CategoryRepository;
import bookstore.service.impl.CategoryServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryMapper categoryMapper;

    @Test
    @DisplayName("Check correct retrieval of category list")
    void findAll_ValidDatabase_ReturnsCategoryDtoList() {
        Category category1 = getCategory1();
        Category category2 = getCategory2();
        CategoryDto categoryDto1 = getCategoryDto1();
        CategoryDto categoryDto2 = getCategoryDto2();
        List<Category> categoryList = List.of(category1, category2);
        Page<Category> page = new PageImpl<>(categoryList);
        PageRequest pageRequest = PageRequest.of(0, 20);

        when(categoryRepository.findAll(pageRequest)).thenReturn(page);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);
        List<CategoryDto> expectedCategoryDtoList = List.of(categoryDto1, categoryDto2);

        List<CategoryDto> actualCategoryDtoList = categoryService.findAll(pageRequest);
        assertEquals(expectedCategoryDtoList, actualCategoryDtoList);
    }

    @Test
    @DisplayName("Check behavior of findAll method for empty database")
    void findAll_EmptyDatabase_ReturnsEmptyPage() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Category> emptyPage = new PageImpl<>(Collections.emptyList());

        when(categoryRepository.findAll(pageRequest)).thenReturn(emptyPage);

        List<CategoryDto> actualCategoryDtoList = categoryService.findAll(pageRequest);
        assertTrue(actualCategoryDtoList.isEmpty());
    }

    @Test
    @DisplayName("Check correct retrieval of category by ID")
    void getById_ValidId_ReturnsCategoryDto() {
        Long categoryId = 1L;
        Category category = getCategory1();
        CategoryDto expectedCategoryDto = getCategoryDto1();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedCategoryDto);

        CategoryDto actualCategoryDto = categoryService.getById(categoryId);
        assertEquals(expectedCategoryDto, actualCategoryDto);
    }

    @Test
    @DisplayName("Check throwing EntityNotFoundException for invalid ID")
    void getById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 5L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(invalidId);
        });
        assertEquals("Can`t find category by id " + invalidId, exception.getMessage());
    }

    @Test
    @DisplayName("Check correct saving of a new category")
    void save_ValidCategory_ReturnsCategoryDto() {
        Category category = getCategory1();
        CreateCategoryRequestDto createRequestDto = getCreateCategoryRequestDto();
        CategoryDto expectedCategoryDto = getCategoryDto1();

        when(categoryMapper.toCategory(createRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedCategoryDto);

        CategoryDto actualCategoryDto = categoryService.save(createRequestDto);
        assertEquals(expectedCategoryDto, actualCategoryDto);
    }

    @Test
    @DisplayName("Check correct updating of category by ID")
    void updateById_ValidId_ReturnsCategoryDto() {
        Long categoryId = 2L;
        String categoryName = "Horror";
        String categoryDescription = "Horror stories";
        Category category = getCategory2();
        CreateCategoryRequestDto requestDto = getCreateCategoryRequestDto();
        CategoryDto expectedCategoryDto = new CategoryDto(categoryId, categoryName, categoryDescription);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedCategoryDto);

        CategoryDto actualCategoryDto = categoryService.updateById(2L, requestDto);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategory(requestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
        assertEquals(expectedCategoryDto, actualCategoryDto);
    }

    @Test
    @DisplayName("Check throwing EntityNotFoundException for invalid ID in updateById method")
    void updateById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 5L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> {categoryService.getById(invalidId);
        });
        assertEquals("Can`t find category by id " + invalidId, exception.getMessage());
    }

    @Test
    @DisplayName("Check correct deletion of category by ID")
    void deleteById_ValidId_Success() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    private static Category getCategory1() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        category.setDescription("Horror stories");
        return category;
    }

    private static Category getCategory2() {
        Category category = new Category();
        category.setId(2L);
        category.setName("Thriller");
        category.setDescription("Thrilling  suspense novels");
        return category;
    }

    private static CategoryDto getCategoryDto1() {
        return new CategoryDto(1L, "Horror", "Horror stories");
    }

    private static CategoryDto getCategoryDto2() {
        return new CategoryDto(2L, "Thriller", "Thrilling  suspense novels");
    }

    private static CreateCategoryRequestDto getCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Horror", "Horror stories"
        );
    }
}
