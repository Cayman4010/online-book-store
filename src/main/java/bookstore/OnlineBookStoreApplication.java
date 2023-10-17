package bookstore;

import bookstore.model.Book;
import bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book newBook = new Book();
            newBook.setAuthor("Jack London");
            newBook.setTitle("Martin Eden");
            newBook.setCoverImage("Custom Image");
            newBook.setDescription("Book for motivation to learn");
            newBook.setPrice(BigDecimal.valueOf(400));
            newBook.setIsbn("9782897177867");
            bookService.save(newBook);
            System.out.println(bookService.findAll());
        };
    }
}
