package bookstore.service.impl;

import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CreateCategoryRequestDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CategoryMapper;
import bookstore.model.Category;
import bookstore.repository.CategoryRepository;
import bookstore.service.CategoryService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id).stream()
                .map(categoryMapper::toDto)
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Can`t find category by id " + id));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        return categoryMapper.toDto(categoryRepository
                .save(categoryMapper.toCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateById(Long id, CreateCategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can`t find category by id " + id));
        categoryMapper.updateCategory(requestDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
