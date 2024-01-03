
package bookstore.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {
        "classpath:database/add-categories.sql",
        "classpath:database/add-books.sql",
        "classpath:database/add-books-categories.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/delete-all-books-categories-relationships.sql",
        "classpath:database/delete-all-books.sql",
        "classpath:database/delete-all-categories.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
            ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a book with valid request DTO")
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = getCreateBookRequestDto();
        BookDto expected = getFrankensteinDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                post("/books")
                .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(expected.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getCoverImage(), actual.getCoverImage());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve all books from a valid database")
    void getAll_ValidDatabase_Success() throws Exception {
        BookDto theShiningDto = getTheShiningDto();
        BookDto draculaDto = getDraculaDto();
        List<BookDto> expected = List.of(theShiningDto, draculaDto);
        PageRequest pageRequest = PageRequest.of(0, 20);

        String jsonRequest = objectMapper.writeValueAsString(pageRequest);

        MvcResult mvcResult = mockMvc.perform(
                        get("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getAuthor(), actual.get(0).getAuthor());
        assertEquals(expected.get(0).getIsbn(), actual.get(0).getIsbn());
        assertEquals(expected.get(0).getPrice(), actual.get(0).getPrice());
        assertEquals(expected.get(0).getCoverImage(), actual.get(0).getCoverImage());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getCategoryIds(), actual.get(0).getCategoryIds());
        assertEquals(expected.get(1).getId(), actual.get(1).getId());
        assertEquals(expected.get(1).getTitle(), actual.get(1).getTitle());
        assertEquals(expected.get(1).getAuthor(), actual.get(1).getAuthor());
        assertEquals(expected.get(1).getIsbn(), actual.get(1).getIsbn());
        assertEquals(expected.get(1).getPrice(), actual.get(1).getPrice());
        assertEquals(expected.get(1).getCoverImage(), actual.get(1).getCoverImage());
        assertEquals(expected.get(1).getDescription(), actual.get(1).getDescription());
        assertEquals(expected.get(1).getCategoryIds(), actual.get(1).getCategoryIds());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve book by valid ID")
    void getBookById_ValidId_Success() throws Exception {
        Long id = 1L;
        BookDto expected = getTheShiningDto();

        MvcResult mvcResult = mockMvc.perform(
                        get("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCoverImage(), actual.getCoverImage());
        assertEquals(expected.getCategoryIds(), actual.getCategoryIds());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve book by invalid ID - Not Found")
    void getBookById_InvalidId_NotFound() throws Exception {
        Long id = 5L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @DisplayName("Delete book by valid ID")
    void deleteById_ValidId_Success() throws Exception {
        Long id = 1L;

        mockMvc.perform(
                delete("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(
                get("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @DisplayName("Delete book by invalid ID - No Content")
    void deleteById_InvalidId_NoContent() throws Exception {
        Long id = 5L;

        mockMvc.perform(
                        delete("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update book with valid ID and request DTO")
    void updateBook_ValidIdAndRequestDto_Success() throws Exception {
        Long id = 2L;
        CreateBookRequestDto requestDto = getCreateBookRequestDto();
        BookDto expected = getFrankensteinDto();
        expected.setId(2L);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/books/{id}", id)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCoverImage(), actual.getCoverImage());
        assertEquals(expected.getCategoryIds(), actual.getCategoryIds());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update book with invalid ID - Not Found")
    void updateBook_InvalidBookId_NotFound() throws Exception {
        Long id = 5L;
        CreateBookRequestDto requestDto = getCreateBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/books/{id}", id)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private static CreateBookRequestDto getCreateBookRequestDto() {
        return new CreateBookRequestDto("Frankenstein", "Mary Shelley",
                "978-0-553-21234-5", BigDecimal.valueOf(300), "Classic horror novel",
                "cover_frankenstein.jpg", Set.of(1L));
    }

    private static BookDto getFrankensteinDto() {
        BookDto bookDto = new BookDto();
        Long id = 3L;
        Long categoryId = 1L;
        bookDto.setId(id);
        bookDto.setTitle("Frankenstein");
        bookDto.setAuthor("Mary Shelley");
        bookDto.setIsbn("978-0-553-21234-5");
        bookDto.setPrice(BigDecimal.valueOf(300));
        bookDto.setDescription("Classic horror novel");
        bookDto.setCoverImage("cover_frankenstein.jpg");
        bookDto.setCategoryIds(List.of(categoryId));
        return bookDto;
    }

    private static BookDto getTheShiningDto() {
        BookDto bookDto = new BookDto();
        Long id = 1L;
        bookDto.setId(id);
        bookDto.setTitle("The Shining");
        bookDto.setAuthor("Stephen King");
        bookDto.setIsbn("978-0-385-12167-5");
        bookDto.setPrice(BigDecimal.valueOf(199.99));
        bookDto.setDescription("A terrifying tale");
        bookDto.setCoverImage("cover_shining.jpg");
        bookDto.setCategoryIds(List.of(id));
        return bookDto;
    }

    private static BookDto getDraculaDto() {
        BookDto bookDto = new BookDto();
        Long bookId = 2L;
        Long categoryId = 1L;
        bookDto.setId(bookId);
        bookDto.setTitle("Dracula");
        bookDto.setAuthor("Bram Stoker");
        bookDto.setIsbn("978-0-553-21277-6");
        bookDto.setPrice(BigDecimal.valueOf(250).setScale(2));
        bookDto.setDescription("Classic vampire story");
        bookDto.setCoverImage("cover_dracula.jpg");
        bookDto.setCategoryIds(List.of(categoryId));
        return bookDto;
    }
}
