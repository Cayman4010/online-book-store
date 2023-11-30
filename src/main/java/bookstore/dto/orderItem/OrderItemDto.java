package bookstore.dto.orderItem;

public record OrderItemDto (
        Long id,
        Long bookId,
        int quantity
) {
}
