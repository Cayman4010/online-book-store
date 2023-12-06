package bookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRequestCartItemDto(
        @NotNull @Positive
        Long bookId,

        @NotNull @Positive
        int quantity
) {
}
