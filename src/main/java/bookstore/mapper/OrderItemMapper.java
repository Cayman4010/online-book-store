package bookstore.mapper;

import bookstore.dto.orderItem.OrderItemDto;
import bookstore.model.OrderItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface OrderItemMapper {
    OrderItem toOrderItem(OrderItemDto orderItemDto);

    OrderItemDto toDto(OrderItem orderItem);
}
