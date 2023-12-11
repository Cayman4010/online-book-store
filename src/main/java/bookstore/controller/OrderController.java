package bookstore.controller;

import bookstore.dto.order.CreateOrderRequestDto;
import bookstore.dto.order.OrderDto;
import bookstore.dto.order.UpdateOrderRequestDto;
import bookstore.dto.orderitem.OrderItemDto;
import bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API", description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order",
            description = "Create a new order")
    @PreAuthorize("hasRole('USER')")
    public OrderDto createOrder(@RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.createOrder(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders",
            description = "Get a set of orders of user")
    @PreAuthorize("hasRole('USER')")
    public Set<OrderDto> getOrdersByUserId() {
        return orderService.getOrders();
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Update an order",
            description = "Update order status")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto updateOrderStatus(@PathVariable @Positive Long orderId,
                                      @Valid @RequestBody UpdateOrderRequestDto requestDto) {
        return orderService.updateOrder(orderId, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get orders from an order",
            description = "Get a set of order items from an specific order")
    @PreAuthorize("hasRole('USER')")
    public Set<OrderItemDto> getOrderItemsFromOrder(@PathVariable @Positive Long orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get an order item from an order",
            description = "Get an specific order item from an specific order")
    @PreAuthorize("hasRole('USER')")
    public OrderItemDto getOrderItemByIdAndOrderId(@PathVariable @Positive Long orderId,
                                                   @PathVariable @Positive Long itemId) {
        return orderService.getOrderItemFromOrder(orderId, itemId);
    }
}
