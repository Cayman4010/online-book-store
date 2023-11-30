package bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateOrderRequestDto(@NotBlank @Size(max = 30) String status) {
}
