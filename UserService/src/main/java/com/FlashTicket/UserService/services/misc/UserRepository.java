package com.FlashTicket.UserService.services.misc;

import com.FlashTicket.UserService.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
