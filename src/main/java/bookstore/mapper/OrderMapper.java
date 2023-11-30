package bookstore.mapper;

import bookstore.dto.order.OrderDto;
import bookstore.dto.orderItem.OrderItemDto;
import bookstore.model.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface OrderMapper {
    Order toOrder(OrderItemDto orderItemDto);

    OrderDto toDto(OrderItemDto orderItemDto);
}
