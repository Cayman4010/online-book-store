package bookstore.dto.shoppingcart;

import bookstore.model.CartItem;
import java.util.Set;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItem> cartItems
) {
}
