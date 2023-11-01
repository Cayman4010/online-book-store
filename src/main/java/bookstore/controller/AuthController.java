package bookstore.controller;

import bookstore.dto.user.UserRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @PostMapping("/register")
    public void register(@RequestBody @Valid UserRegistrationRequest request) {

    }
}
