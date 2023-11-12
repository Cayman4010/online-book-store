package bookstore.service;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);

    List<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);
}
