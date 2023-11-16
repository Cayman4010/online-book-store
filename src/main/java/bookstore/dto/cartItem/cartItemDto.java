package bookstore.dto.cartItem;

public record cartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
        ) {
}
