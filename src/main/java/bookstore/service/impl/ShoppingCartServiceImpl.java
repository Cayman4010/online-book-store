package bookstore.service.impl;

import bookstore.dto.cartitem.CartItemDto;
import bookstore.dto.cartitem.CreateRequestCartItemDto;
import bookstore.dto.cartitem.UpdateCartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CartItemMapper;
import bookstore.mapper.ShoppingCartMapper;
import bookstore.model.Book;
import bookstore.model.CartItem;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import bookstore.repository.BookRepository;
import bookstore.repository.CartItemRepository;
import bookstore.repository.ShoppingCartRepository;
import bookstore.repository.UserRepository;
import bookstore.service.ShoppingCartService;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartDto findById(Long id) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);
        if (shoppingCart.isPresent()) {
            return shoppingCartMapper.toDto(shoppingCart.get());
        } else {
            throw new EntityNotFoundException("Can`t find a shopping cart for user with id " + id);
        }
    }

    @Transactional
    @Override
    public ShoppingCartDto addCartItemToCart(Long id, CreateRequestCartItemDto requestCartItemDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseGet(() -> createShoppingCart(id));
        Book book = bookRepository.findById(requestCartItemDto.bookId()).orElseThrow(()
                -> new EntityNotFoundException("Can`t find book by id "
                + requestCartItemDto.bookId()));
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(requestCartItemDto.quantity());
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public CartItemDto updateQuantity(Long id, Long cartItemId,
                                      UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(()
                -> new EntityNotFoundException("Can`t find book by id " + cartItemId));
        cartItem.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Transactional
    @Override
    public ShoppingCartDto deleteCartItem(Long id, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return findById(id);
    }

    @Override
    public void emptyCart(ShoppingCart shoppingCart) {
        shoppingCart.setCartItems(Collections.emptySet());
        cartItemRepository.deleteAllByShoppingCartId(shoppingCart.getId());
    }

    public ShoppingCart createShoppingCart(Long userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user by id + " + userId));
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }
}
