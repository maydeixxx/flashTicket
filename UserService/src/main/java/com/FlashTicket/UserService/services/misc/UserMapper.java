package com.FlashTicket.UserService.services.misc;

import com.FlashTicket.UserService.models.domain.UserDomain;
import com.FlashTicket.UserService.models.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDomain entityToDomain(User entity) {
        return UserDomain.restoreFromExisting(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public User domainToEntity(UserDomain domain) {
        User user = new User();
        user.setId(domain.getId().value());
        user.setEmail(domain.getEmail());
        user.setPasswordHash(domain.getPasswordHash());
        user.setCreatedAt(domain.getCreatedAt());
        user.setUpdatedAt(domain.getUpdatedAt());
        return user;
    }
}
