package bookstore.mapper;

import bookstore.dto.shoppingCart.ShoppingCartDto;
import bookstore.model.ShoppingCart;

public interface ShoppingCartMapper {


    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
