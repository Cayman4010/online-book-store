package bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
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
public class CategoryControllerTest {

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
    @DisplayName("Create a category with valid request DTO")
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = getCreateCategoryRequestDto();
        CategoryDto expected = getFantasyDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);
        Assertions.assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.description(), actual.description());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve all categories from a valid database")
    void getAll_ValidDatabase_Success() throws Exception {
        CategoryDto horrorDto = getHorrorDto();
        CategoryDto thrillerDto = getThrillerDto();
        List<CategoryDto> expected = List.of(horrorDto, thrillerDto);
        PageRequest pageRequest = PageRequest.of(0, 20);

        String jsonRequest = objectMapper.writeValueAsString(pageRequest);

        MvcResult mvcResult = mockMvc.perform(
                        get("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                });
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).id(), actual.get(0).id());
        assertEquals(expected.get(0).name(), actual.get(0).name());
        assertEquals(expected.get(0).description(), actual.get(0).description());
        assertEquals(expected.get(1).id(), actual.get(1).id());
        assertEquals(expected.get(1).name(), actual.get(1).name());
        assertEquals(expected.get(1).description(), actual.get(1).description());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve category by valid ID")
    void getCategoryById_ValidId_Success() throws Exception {
        Long id = 1L;
        CategoryDto expected = getHorrorDto();

        MvcResult mvcResult = mockMvc.perform(
                        get("/categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.description(), actual.description());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve category by invalid ID - Not Found")
    void getCategoryById_InvalidId_NotFound() throws Exception {
        Long id = 5L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update category with valid ID and request DTO")
    void updateCategory_ValidIdAndRequestDto_Success() throws Exception {
        Long id = 2L;
        CreateCategoryRequestDto requestDto = getCreateCategoryRequestDto();
        CategoryDto expected = new CategoryDto(2L, "Fantasy", "Fantasy novels");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/categories/{id}", id)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.description(), actual.description());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update category with invalid ID - Not Found")
    void updateCategory_InvalidCategoryId_NotFound() throws Exception {
        Long id = 5L;
        CreateCategoryRequestDto requestDto = getCreateCategoryRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/categories/{id}", id)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @DisplayName("Delete category by valid ID")
    void deleteCategory_ValidId_Success() throws Exception {
        Long id = 1L;

        mockMvc.perform(
                        delete("/categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(
                        get("/categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete category by invalid ID - No Content")
    void deleteCategory_InvalidCategoryId_NoContent() throws Exception {
        Long id = 5L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve books by category ID from a valid database")
    void getBooksByCategoryId_ValidDatabaseAndCategoryId_Success() throws Exception {
        Long categoryId = 1L;
        BookDtoWithoutCategoryIds theShiningDtoWithoutCategoryIds =
                getTheShiningDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds draculaDtoWithoutCategoryIds = getDraculaDtoWithoutCategoryIds();
        List<BookDtoWithoutCategoryIds> expected =
                List.of(theShiningDtoWithoutCategoryIds, draculaDtoWithoutCategoryIds);

        MvcResult mvcResult = mockMvc.perform(
                        get("/categories/{id}/books", categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                });
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).id(), actual.get(0).id());
        assertEquals(expected.get(0).title(), actual.get(0).title());
        assertEquals(expected.get(0).author(), actual.get(0).author());
        assertEquals(expected.get(0).isbn(), actual.get(0).isbn());
        assertEquals(expected.get(0).price(), actual.get(0).price());
        assertEquals(expected.get(0).coverImage(), actual.get(0).coverImage());
        assertEquals(expected.get(0).description(), actual.get(0).description());
        assertEquals(expected.get(1).id(), actual.get(1).id());
        assertEquals(expected.get(1).title(), actual.get(1).title());
        assertEquals(expected.get(1).author(), actual.get(1).author());
        assertEquals(expected.get(1).isbn(), actual.get(1).isbn());
        assertEquals(expected.get(1).price(), actual.get(1).price());
        assertEquals(expected.get(1).coverImage(), actual.get(1).coverImage());
        assertEquals(expected.get(1).description(), actual.get(1).description());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve books by invalid category ID - returns empty list")
    void getBooksByCategoryId_InvalidCategoryId_ReturnsEmptyList() throws Exception {
        Long categoryId = 5L;
        List<BookDtoWithoutCategoryIds> expected = List.of();

        MvcResult mvcResult = mockMvc.perform(
                        get("/categories/{id}/books", categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<BookDtoWithoutCategoryIds>>() {
                });
        assertEquals(expected, actual);
    }

    private static CreateCategoryRequestDto getCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Fantasy", "Fantasy novels"
        );
    }

    private static CategoryDto getFantasyDto() {
        return new CategoryDto(3L, "Fantasy", "Fantasy novels");
    }

    private static CategoryDto getHorrorDto() {
        return new CategoryDto(1L, "Horror", "Horror stories");
    }

    private static CategoryDto getThrillerDto() {
        return new CategoryDto(2L, "Thriller", "Thrilling  suspense novels");
    }

    private static BookDtoWithoutCategoryIds getDraculaDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds(
                2L, "Dracula", "Bram Stoker", "978-0-553-21277-6",
                BigDecimal.valueOf(250).setScale(2), "Classic vampire story", "cover_dracula.jpg"
        );
    }

    private static BookDtoWithoutCategoryIds getTheShiningDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds(
                1L, "The Shining", "Stephen King", "978-0-385-12167-5", BigDecimal.valueOf(199.99),
                "A terrifying tale", "cover_shining.jpg"
        );
    }
}
