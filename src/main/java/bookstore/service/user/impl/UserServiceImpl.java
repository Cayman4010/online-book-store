package bookstore.service.user.impl;

import bookstore.dto.user.UserRegistrationRequestDto;
import bookstore.dto.user.UserResponseDto;
import bookstore.exception.RegistrationException;
import bookstore.mapper.UserMapper;
import bookstore.model.RoleName;
import bookstore.model.User;
import bookstore.repository.RoleRepository;
import bookstore.repository.UserRepository;
import bookstore.service.user.UserService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Collections.singleton(roleRepository.findByName(RoleName.USER)));
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
