package com.FlashTicket.UserService.models.domain;

import com.FlashTicket.UserService.models.domain.enums.Role;
import com.FlashTicket.UserService.models.domain.valueObjects.UserId;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Getter
public class UserDomain {
    private final UserId id;
    private String email;
    private String passwordHash;
    private Role role;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDomain(UserId id, String email, String passwordHash, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("Password is required");
        if (createdAt == null) throw new IllegalArgumentException("Created at is required");
        if (role == null) throw new IllegalArgumentException("Role is required");

        this.id = id;
        this.email = email.toLowerCase();
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserDomain createUser(String email, String passwordHash) {
        UserId id = UserId.generateUserId();
        LocalDateTime timestamp = LocalDateTime.now();

        return new UserDomain(id, email, passwordHash, Role.USER, timestamp, timestamp);
    }

    public static UserDomain restoreFromExisting(UUID rawId, String email, String passwordHash, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        UserId id = new UserId(rawId);
        return new UserDomain(id, email, passwordHash, role, createdAt, updatedAt);
    }

    public void updateEmail(String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");
        if (email.equalsIgnoreCase(this.email)) throw new IllegalArgumentException("You already have this email");
        this.email = email.toLowerCase();
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePassword(String newPasswordHash) {
        if (newPasswordHash == null) throw new IllegalArgumentException("New password is required");
        this.passwordHash = newPasswordHash;
        this.updatedAt = LocalDateTime.now();
    }

    public void upgradeUser() {
        if (this.role.equals(Role.ADMIN)) {
            log.warn("User already 'ADMIN'");
            return;
        }

        this.role = Role.ADMIN;
        this.updatedAt = LocalDateTime.now();
    }

    public void downgradeUser() {
        if (this.role.equals(Role.USER)) {
            log.warn("User already 'USER'");
        }

        this.role = Role.USER;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserDomain that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
