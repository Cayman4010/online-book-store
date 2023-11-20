package bookstore.service;

import bookstore.dto.cartItem.CreateRequestCartItemDto;
import bookstore.dto.shoppingCart.ShoppingCartDto;
import org.springframework.stereotype.Service;

@Service
public interface ShoppingCartService {
    ShoppingCartDto findById(Long id);

    ShoppingCartDto addCartItemToCart(Long id, CreateRequestCartItemDto requestCartItemDto);

    ShoppingCartDto updateQuantity(Long id, Long cartItemId, int quantity);

    ShoppingCartDto deleteCartItem(Long id, Long cartItemId);
}
