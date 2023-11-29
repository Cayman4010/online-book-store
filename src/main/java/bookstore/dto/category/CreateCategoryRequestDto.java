package bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank @Size(max = 255)
        String name,
        @Size(max = 255)
        String description
) {
}
