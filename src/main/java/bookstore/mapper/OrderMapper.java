package bookstore.mapper;

import bookstore.dto.order.OrderDto;
import bookstore.model.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface OrderMapper {
    Order toOrder(OrderDto orderDto);

    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);
}
