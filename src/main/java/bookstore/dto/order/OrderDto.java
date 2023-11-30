package bookstore.dto.order;

import bookstore.dto.orderItem.OrderItemDto;
import bookstore.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItems,
        LocalDateTime orderTime,
        BigDecimal total,
        String status
) {
}
