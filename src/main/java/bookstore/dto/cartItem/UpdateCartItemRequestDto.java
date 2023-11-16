package bookstore.dto.cartItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequestDto(@Positive @NotBlank int quantity) {
}
