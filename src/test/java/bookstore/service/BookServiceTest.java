package bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.dto.category.CategoryDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.BookMapper;
import bookstore.model.Book;
import bookstore.model.Category;
import bookstore.repository.BookRepository;
import bookstore.repository.CategoryRepository;
import bookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void save_ValidBook_ReturnsBookDto() {
        CreateBookRequestDto requestDto = getCreateBookRequestDto();
        Book expectedBook = getBook1();
        BookDto expectedBookDto = getBookDto1();
        Category horror = getCategory();

        when(bookMapper.toBook(requestDto)).thenReturn(expectedBook);
        when(categoryRepository.getReferenceById(1L)).thenReturn(horror);
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        when(bookMapper.toDto(expectedBook)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookService.save(requestDto);
        assertEquals(expectedBookDto, actualBookDto);
    }

    @Test
    void findAllByCategoryId_ValidCategoryId_ReturnsBookDtoList() {
        Book book = getBook1();
        Long categoryId = 1L;
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(
                1L, "Dracula", "Bram Stoker", "978-0-553-21277-6", BigDecimal.valueOf(250),
                "Classic vampire story", "cover_dracula.jpg"
        );

        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(categoryId);

        assertEquals(expected, actual);
    }

    @Test
    void findAll_ValidDatabase_ReturnsBookPage() {
        Book book1 = getBook1();
        Book book2 = getBook2();
        BookDto bookDto1 = getBookDto1();
        BookDto bookDto2 = getBookDto2();
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<Book> bookList = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookRepository.findAll(pageRequest)).thenReturn(bookPage);
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);
        List<BookDto> expected = List.of(bookDto1, bookDto2);
        List<BookDto> actual = bookService.findAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void findAll_EmptyDatabase_ReturnsEmptyPage() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findAll(pageRequest)).thenReturn(emptyPage);

        List<BookDto> actualBookDtoList = bookService.findAll(pageRequest);
        assertTrue(actualBookDtoList.isEmpty());
    }

    @Test
    void getById_ValidId_ReturnsBookDto() {
        Long bookId = 1L;
        Book book = getBook1();
        BookDto expectedBookDto = getBookDto1();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookService.getById(bookId);
        assertEquals(expectedBookDto, actualBookDto);
    }

    @Test
    void getById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 5L;

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(invalidId));
        assertEquals("Can`t find book by id" + invalidId, exception.getMessage());
    }

    @Test
    void deleteById_ValidId_Success() {
        Long bookId = 1L;
        bookService.deleteById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void updateById_ValidId_ReturnsBookDto() {
        Long bookId = 2L;
        CreateBookRequestDto requestDto = getCreateBookRequestDto();
        Book book = getBook2();
        BookDto expectedBookDto = getBookDto1();
        expectedBookDto.setId(2L);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookService.updateById(bookId, requestDto);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateBook(requestDto, book);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
        assertEquals(expectedBookDto, actualBookDto);
    }

    @Test
    void updateById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 5L;

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(invalidId));
        assertEquals("Can`t find book by id" + invalidId, exception.getMessage());
    }

    private static BookDto getBookDto1() {
        BookDto bookDto = new BookDto();
        Long id = 1L;
        bookDto.setId(id);
        bookDto.setTitle("Dracula");
        bookDto.setAuthor("Bram Stoker");
        bookDto.setIsbn("978-0-553-21277-6");
        bookDto.setPrice(BigDecimal.valueOf(250));
        bookDto.setDescription("Classic vampire story");
        bookDto.setCoverImage("cover_dracula.jpg");
        bookDto.setCategoryIds(List.of(id));
        return bookDto;
    }

    private static BookDto getBookDto2() {
        BookDto bookDto = new BookDto();
        Long bookId = 2L;
        Long categoryId = 1L;
        bookDto.setId(bookId);
        bookDto.setTitle("The Shining");
        bookDto.setAuthor("Stephen King");
        bookDto.setIsbn("978-0-385-12167-5");
        bookDto.setPrice(BigDecimal.valueOf(199.99));
        bookDto.setDescription("A terrifying tale");
        bookDto.setCoverImage("cover_shining.jpg");
        bookDto.setCategoryIds(List.of(categoryId));
        return bookDto;
    }

    private static CreateBookRequestDto getCreateBookRequestDto() {
        return new CreateBookRequestDto("Dracula", "Bram Stoker",
                "978-0-553-21277-6", BigDecimal.valueOf(250), "Classic vampire story",
                "cover_dracula.jpg", Set.of(1L));
    }

    private static Book getBook1() {
        Book book = new Book();
        Long id = 1L;
        book.setId(id);
        book.setTitle("Dracula");
        book.setAuthor("Bram Stoker");
        book.setIsbn("978-0-553-21277-6");
        book.setPrice(BigDecimal.valueOf(250));
        book.setDescription("Classic vampire story");
        book.setCoverImage("cover_dracula.jpg");
        book.setCategories(Set.of(getCategory()));
        return book;
    }

    private static Book getBook2() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("The Shining");
        book.setAuthor("Stephen King");
        book.setIsbn("978-0-385-12167-5");
        book.setPrice(new BigDecimal("199.99"));
        book.setDescription("A terrifying tale");
        book.setCoverImage("cover_shining.jpg");
        book.setCategories(Set.of(getCategory()));
        return book;
    }

    private static Category getCategory() {
        Category horror = new Category();
        horror.setId(1L);
        horror.setName("Horror");
        horror.setDescription("Horror stories");
        return horror;
    }
}
