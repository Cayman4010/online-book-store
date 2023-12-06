package bookstore.service;

import bookstore.dto.cartitem.CartItemDto;
import bookstore.dto.cartitem.CreateRequestCartItemDto;
import bookstore.dto.cartitem.UpdateCartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.model.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public interface ShoppingCartService {
    ShoppingCartDto findById(Long id);

    ShoppingCartDto addCartItemToCart(Long id, CreateRequestCartItemDto requestCartItemDto);

    CartItemDto updateQuantity(Long id, Long cartItemId, UpdateCartItemRequestDto requestDto);

    void deleteCartItem(Long cartItemId);

    void clearShoppingCart(ShoppingCart shoppingCart);
}
