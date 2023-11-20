package bookstore.service.impl;

import bookstore.dto.cartItem.CreateRequestCartItemDto;
import bookstore.dto.shoppingCart.ShoppingCartDto;
import bookstore.repository.ShoppingCartRepository;
import bookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper;


    @Override
    public ShoppingCartDto findById(Long id) {
        return null;
    }

    @Override
    public ShoppingCartDto addCartItemToCart(Long id, CreateRequestCartItemDto requestCartItemDto) {
        return null;
    }

    @Override
    public ShoppingCartDto updateQuantity(Long id, Long cartItemId, int quantity) {
        return null;
    }

    @Override
    public ShoppingCartDto deleteCartItem(Long id, Long cartItemId) {
        return null;
    }
}
