package com.FlashTicket.UserService.services.misc;

import com.FlashTicket.UserService.models.domain.UserDomain;
import com.FlashTicket.UserService.models.dtos.UserUpdateDTO;
import com.FlashTicket.UserService.models.entity.User;
import com.FlashTicket.UserService.models.exceptions.UserExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdateEmailStrategy implements UserUpdateStrategy {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public String getType() {
        return "email";
    }

    @Override
    public void updateUser(UserDomain userDomain, UserUpdateDTO updateDTO) {
        try {
            Optional<User> user = userRepository.findByEmail(updateDTO.newEmail());
            if (user.isPresent()) throw new UserExistsException("Email that you entered already in use, please choose another one");

            userDomain.updateEmail(updateDTO.newEmail());
            userRepository.save(userMapper.domainToEntity(userDomain));
        } catch (IllegalArgumentException | UserExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating email [{}]: {}", userDomain.getEmail(), e.getMessage());
        }
    }
}
