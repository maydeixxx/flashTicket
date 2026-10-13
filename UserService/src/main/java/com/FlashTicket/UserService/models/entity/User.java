package com.FlashTicket.UserService.models.entity;

import com.FlashTicket.UserService.models.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
