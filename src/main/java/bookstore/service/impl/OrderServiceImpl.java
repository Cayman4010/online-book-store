package bookstore.service.impl;

import bookstore.dto.order.CreateOrderRequestDto;
import bookstore.dto.order.OrderDto;
import bookstore.dto.order.UpdateOrderRequestDto;
import bookstore.dto.orderItem.OrderItemDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.OrderItemMapper;
import bookstore.mapper.OrderMapper;
import bookstore.model.CartItem;
import bookstore.model.Order;
import bookstore.model.OrderItem;
import bookstore.model.ShoppingCart;
import bookstore.model.Status;
import bookstore.model.User;
import bookstore.repository.CartItemRepository;
import bookstore.repository.OrderItemRepository;
import bookstore.repository.OrderRepository;
import bookstore.repository.ShoppingCartRepository;
import bookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import bookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public OrderDto createOrder(CreateOrderRequestDto requestDto) {
        Order order = new Order();
        order.setUser(getUser());
        order.setStatus(Status.PENDING);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());
        ShoppingCart shoppingCart = getShoppingCart();
        Set<OrderItem> orderItems = createOrderItems(order, shoppingCart);
        order.setOrderItems(orderItems);
        shoppingCartService.emptyCart(shoppingCart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderDto> getOrders() {
        return orderRepository.findByUserId(getUser().getId()).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderDto updateOrder(Long orderId, UpdateOrderRequestDto requestDto) {
        Order order = getOrder(orderId);
        order.setStatus(Status.valueOf(requestDto.status()));
        return orderMapper.toDto(order);
    }

    @Override
    public Set<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        Order order = getOrder(orderId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("Cant find order by id" + orderId));
    }

    @Override
    public OrderItemDto getOrderItemFromOrder(Long itemId, Long orderId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrder_Id(itemId, orderId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find order item by id " + itemId
                + " and order id " + orderId));
        return orderItemMapper.toDto(orderItem);
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (User) authentication.getPrincipal();
    }

    private ShoppingCart getShoppingCart() {
        Long userId = getUser().getId();
        return shoppingCartRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Can`t find shopping cart for user with id "
        + userId));
    }

    private Set<OrderItem> createOrderItems(Order order, ShoppingCart shoppingCart) {
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Set<OrderItem> orderItems = new HashSet<>();
        for(CartItem cartItem: cartItems) {
            OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);
            orderItem.setOrder(order);
            orderItems.add(orderItemRepository.save(orderItem));
            BigDecimal newTotal = order.getTotal()
                    .add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            order.setTotal(newTotal);
        }
        return orderItems;
    }
}
