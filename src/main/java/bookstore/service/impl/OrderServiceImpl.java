package bookstore.service.impl;

import bookstore.dto.order.CreateOrderRequestDto;
import bookstore.dto.order.OrderDto;
import bookstore.dto.order.UpdateOrderRequestDto;
import bookstore.dto.orderitem.OrderItemDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.OrderItemMapper;
import bookstore.mapper.OrderMapper;
import bookstore.model.CartItem;
import bookstore.model.Order;
import bookstore.model.OrderItem;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import bookstore.repository.OrderItemRepository;
import bookstore.repository.OrderRepository;
import bookstore.repository.ShoppingCartRepository;
import bookstore.service.OrderService;
import bookstore.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;

    @Transactional
    @Override
    public OrderDto createOrder(CreateOrderRequestDto requestDto) {
        Order order = getOrder(requestDto);
        ShoppingCart shoppingCart = getShoppingCart();
        Set<OrderItem> orderItems = createOrderItems(order, shoppingCart);
        order.setOrderItems(orderItems);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderDto> getOrders() {
        return orderRepository.findByUserId(getUser().getId()).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public OrderDto updateOrder(Long orderId, UpdateOrderRequestDto requestDto) {
        Order order = getOrder(orderId);
        order.setStatus(Order.Status.valueOf(requestDto.status()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        Order order = getOrder(orderId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemDto getOrderItemFromOrder(Long itemId, Long orderId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrder_Id(orderId, itemId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find order item by id " + itemId
                + " and order id " + orderId));
        return orderItemMapper.toDto(orderItem);
    }

    private Order getOrder(CreateOrderRequestDto requestDto) {
        Order order = new Order();
        order.setUser(getUser());
        order.setStatus(Order.Status.PENDING);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());
        return order;
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("Cant find order by id" + orderId));
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
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
        BigDecimal newTotal = BigDecimal.ZERO;
        Order savedOrder = orderRepository.save(order);
        for (CartItem cartItem: cartItems) {
            OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);
            orderItem.setOrder(savedOrder);
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
            BigDecimal itemTotal = savedOrderItem.getPrice()
                    .multiply(BigDecimal.valueOf(savedOrderItem.getQuantity()));
            newTotal = newTotal.add(itemTotal);
        }
        savedOrder.getOrderItems().addAll(orderItems);
        savedOrder.setTotal(order.getTotal().add(newTotal));
        return orderItems;
    }
}
