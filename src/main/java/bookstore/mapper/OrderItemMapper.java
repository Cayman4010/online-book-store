package bookstore.mapper;

import bookstore.dto.orderitem.OrderItemDto;
import bookstore.model.CartItem;
import bookstore.model.OrderItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface OrderItemMapper {
    OrderItem toOrderItem(OrderItemDto orderItemDto);

    @Mapping(source = "book.price", target = "price")
    OrderItem toOrderItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}
