package bookstore.controller;

import bookstore.dto.shoppingCart.ShoppingCartDto;
import bookstore.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/cart")
public class ShoppingCartController {

    public ShoppingCartDto getShoppingCart() {
        getUserId();
    }

    private static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }


    public ShoppingCartDto addBookToCart() {
        return
    }

    public ShoppingCartDto deleteBookFromCart(Long id) {

    }

    public ShoppingCartDto updateBooksQuantity(Long id, int quantity) {

    }
}
