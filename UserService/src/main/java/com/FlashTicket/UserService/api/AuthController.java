package com.FlashTicket.UserService.api;

import com.FlashTicket.UserService.models.dtos.AuthDTO;
import com.FlashTicket.UserService.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping()
    public ResponseEntity<String> registerUser(@RequestBody @Valid AuthDTO authDTO) {
        String token = authService.registerUser(authDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid AuthDTO authDTO) {
        String token = authService.login(authDTO);
        return ResponseEntity.ok(token);
    }
}
