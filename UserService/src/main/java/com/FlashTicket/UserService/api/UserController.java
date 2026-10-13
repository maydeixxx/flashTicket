package com.FlashTicket.UserService.api;

import com.FlashTicket.UserService.models.dtos.UserDto;
import com.FlashTicket.UserService.models.dtos.UserUpdateDTO;
import com.FlashTicket.UserService.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/{email}")
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or authentication.principal.email == #email)")
    public ResponseEntity<UserDto> getUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUser(email));
    }

    @PutMapping("/update/{email}")
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or authentication.principal.email == #userUpdateDTO.email())")
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/upgrade/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> upgradeUser(@PathVariable String email) {
        userService.upgradeUser(email);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/downgrade/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> downgradeUser(@PathVariable String email) {
        userService.downgradeUser(email);
        return ResponseEntity.status(204).build();
    }
}
