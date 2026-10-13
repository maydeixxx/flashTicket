package com.FlashTicket.UserService.services.misc;

import com.FlashTicket.UserService.models.domain.UserDomain;
import com.FlashTicket.UserService.models.dtos.UserDTO;
import com.FlashTicket.UserService.models.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDomain entityToDomain(User entity) {
        return UserDomain.restoreFromExisting(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public User domainToEntity(UserDomain domain) {
        User user = new User();
        user.setId(domain.getId().value());
        user.setEmail(domain.getEmail());
        user.setPasswordHash(domain.getPasswordHash());
        user.setRole(domain.getRole());
        user.setCreatedAt(domain.getCreatedAt());
        user.setUpdatedAt(domain.getUpdatedAt());
        return user;
    }

    public UserDTO entityToDto(User entity) {
        return new UserDTO(
                entity.getId(),
                entity.getEmail(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
