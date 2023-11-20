package bookstore.dto.shoppingCart;

import bookstore.model.CartItem;

import java.util.Set;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItem> cartItems
) {
}
