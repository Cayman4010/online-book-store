package bookstore.dto.cartItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateRequestCartItemDto(
        @NotBlank @Positive
        Long bookId,

        @NotBlank @Positive
        int quantity
) {
}
