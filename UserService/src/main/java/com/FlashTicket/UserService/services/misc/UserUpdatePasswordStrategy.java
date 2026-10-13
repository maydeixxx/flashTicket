package com.FlashTicket.UserService.services.misc;

import com.FlashTicket.UserService.models.domain.UserDomain;
import com.FlashTicket.UserService.models.dtos.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUpdatePasswordStrategy implements UserUpdateStrategy {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public String getType() {
        return "password";
    }

    @Override
    public void updateUser(UserDomain userDomain, UserUpdateDTO userUpdateDTO) {
        try {
            if (passwordEncoder.matches(userUpdateDTO.newPassword(), userDomain.getPasswordHash())) throw new IllegalArgumentException("You already have this password");

            String newEncodedPassword = passwordEncoder.encode(userUpdateDTO.newPassword());
            userDomain.updatePassword(newEncodedPassword);

            userRepository.save(userMapper.domainToEntity(userDomain));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating password for [{}]: {}", userDomain.getEmail(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
