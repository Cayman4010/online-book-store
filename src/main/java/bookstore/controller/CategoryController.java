package bookstore.controller;

import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CreateCategoryRequestDto;
import bookstore.service.BookService;
import bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Categories API", description = "Endpoints for managing categories")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new book", description = "Create a new book")
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get all categories",
            description = "Get a list of all available categories")
    @GetMapping
    public List<CategoryDto> getAll(@PageableDefault Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get a category by id", description = "Get a category by id")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable @Positive Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a category by id",
            description = "Update a category by id")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable @Positive Long id,
                                      @RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.updateById(id, requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a category by id",
            description = "Delete a category by id")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get all books by category id",
            description = "Get a list of all books by category id")
    @GetMapping(value = "/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable @Positive Long id) {
        return bookService.findAllByCategoryId(id);
    }
}
