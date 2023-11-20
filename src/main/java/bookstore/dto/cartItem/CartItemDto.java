package bookstore.dto.cartItem;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
        ) {
}
