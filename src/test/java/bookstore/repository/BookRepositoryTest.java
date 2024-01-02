package bookstore.repository;

import bookstore.model.Book;
import bookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findAllByCategoryId_ValidCategoryIdAndExistingBooks_ReturnsBookList() {
        Book theShining = getTheShining();
        Book dracula = getDracula();
        Long categoryId = 1L;
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId);
        List<Book> expected = List.of(theShining, dracula);

        Assertions.assertEquals(actual.size(), expected.size());
        Assertions.assertEquals(actual.get(0).getId(), expected.get(0).getId());
        Assertions.assertEquals(actual.get(0).getTitle(), expected.get(0).getTitle());
        Assertions.assertEquals(actual.get(0).getAuthor(), expected.get(0).getAuthor());
        Assertions.assertEquals(actual.get(0).getIsbn(), expected.get(0).getIsbn());
        Assertions.assertEquals(actual.get(0).getPrice(), expected.get(0).getPrice());
        Assertions.assertEquals(actual.get(0).getDescription(), expected.get(0).getDescription());
        Assertions.assertEquals(actual.get(0).getCoverImage(), expected.get(0).getCoverImage());
        Assertions.assertEquals(actual.get(0).isDeleted(), expected.get(0).isDeleted());
        Assertions.assertEquals(actual.get(0).getCategories().stream().map(Category::getName).findFirst()
                        .orElse(null),
                expected.get(0).getCategories().stream().map(Category::getName).findFirst().orElse(null));
    }

    @Test
    void findAllByCategoryId_ValidCategoryIdAndEmptyBookList_ReturnsEmptyBookList() {
        List<Book> actual = bookRepository.findAllByCategoryId(2L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findAllByCategoryId_InValidCategoryId_ReturnsEmptyBookList() {
        List<Book> actual = bookRepository.findAllByCategoryId(20L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @NotNull
    private static Book getDracula() {
        Book dracula = new Book();
        dracula.setId(2L);
        dracula.setTitle("Dracula");
        dracula.setAuthor("Bram Stoker");
        dracula.setIsbn("978-0-553-21277-6");
        dracula.setPrice(BigDecimal.valueOf(250));
        dracula.setDescription("Classic vampire story");
        dracula.setCoverImage("cover_dracula.jpg");
        dracula.setDeleted(false);
        dracula.setCategories(Set.of(getHorror()));
        return dracula;
    }

    @NotNull
    private static Book getTheShining() {
        Book theShining = new Book();
        theShining.setId(1L);
        theShining.setTitle("The Shining");
        theShining.setAuthor("Stephen King");
        theShining.setIsbn("978-0-385-12167-5");
        theShining.setPrice(new BigDecimal("199.99"));
        theShining.setDescription("A terrifying tale");
        theShining.setCoverImage("cover_shining.jpg");
        theShining.setDeleted(false);
        theShining.setCategories(Set.of(getHorror()));
        return theShining;
    }

    @NotNull
    private static Category getHorror() {
        Category horror = new Category();
        horror.setName("Horror");
        horror.setDescription("Horror stories");
        horror.setDeleted(false);
        return horror;
    }
}
