package bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

public record CreateBookRequestDto(
        @NotBlank @Size(max = 255)
        String title,
        @NotBlank @Size(max = 255)
        String author,
        @NotBlank @Size(max = 255)
        String isbn,
        @PositiveOrZero @NotNull
        BigDecimal price,
        @Size(max = 255)
        String description,
        @Size(max = 255)
        String coverImage,
        Set<Long> categoryIds) {
}
