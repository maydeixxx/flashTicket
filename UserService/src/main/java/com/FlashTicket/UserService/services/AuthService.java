package com.FlashTicket.UserService.services;

import com.FlashTicket.UserService.models.domain.UserDomain;
import com.FlashTicket.UserService.models.dtos.AuthDTO;
import com.FlashTicket.UserService.models.entity.User;
import com.FlashTicket.UserService.models.exceptions.AuthenticationErrorException;
import com.FlashTicket.UserService.models.exceptions.UserNotFoundException;
import com.FlashTicket.UserService.services.misc.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public String registerUser(AuthDTO newUser) {
        try {
            String encodedPassword = passwordEncoder.encode(newUser.password());
            UserDomain user = UserDomain.createUser(newUser.email(), encodedPassword);
            return jwtService.generateToken(user.getEmail(), user.getRole());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error registering user [{}]: {}", newUser.email(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String login(AuthDTO loginUser) {
        try {
            User user = userRepository.findByEmail(loginUser.email()).orElseThrow(() -> new UserNotFoundException("User [%s] not found".formatted(loginUser.email())));
            String passwordHash = user.getPasswordHash();
            String rawPassword = loginUser.password();

            if (passwordEncoder.matches(rawPassword, passwordHash))
                throw new AuthenticationErrorException("Password is incorrect");

            return jwtService.generateToken(user.getEmail(), user.getRole());
        } catch (UserNotFoundException | AuthenticationErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error logging in [{}]: {}", loginUser.email(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
