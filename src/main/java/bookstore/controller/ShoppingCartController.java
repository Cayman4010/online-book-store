package bookstore.controller;

import bookstore.dto.cartitem.CartItemDto;
import bookstore.dto.cartitem.CreateRequestCartItemDto;
import bookstore.dto.cartitem.UpdateCartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.model.User;
import bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart API", description = "Endpoints for managing shopping carts")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get a user`s shopping cart", description = "Get a shopping cart by id")
    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.findById(getUserId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Create a new cart item", description = "Create a new cart item")
    @PostMapping
    public ShoppingCartDto addCartItem(@RequestBody @Valid CreateRequestCartItemDto requestDto) {
        return shoppingCartService.addCartItemToCart(getUserId(), requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Delete a cart item by id",
            description = "Delete a cart item by id")
    @DeleteMapping("/cart-items/{cartItemId}")
    public void deleteCartItem(@Positive @PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Update a cart item",
            description = "Update book quantity in a cart item")
    @PutMapping("/cart-items/{cartItemId}")
    public CartItemDto updateBookQuantity(@Positive @PathVariable Long cartItemId,
                                          @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateQuantity(getUserId(), cartItemId, requestDto);
    }

    private static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((User) authentication.getPrincipal()).getId();
    }
}
