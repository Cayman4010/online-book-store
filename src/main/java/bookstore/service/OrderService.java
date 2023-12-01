package bookstore.service;

import bookstore.dto.order.CreateOrderRequestDto;
import bookstore.dto.order.OrderDto;
import bookstore.dto.order.UpdateOrderRequestDto;
import bookstore.dto.orderItem.OrderItemDto;
import java.util.Set;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto requestDto);

    Set<OrderDto> getOrders();

    OrderDto updateOrder(Long orderId, UpdateOrderRequestDto requestDto);

    Set<OrderItemDto> getOrderItemsByOrderId(Long orderId);

    OrderItemDto getOrderItemFromOrder(Long orderId, Long itemId);
}
