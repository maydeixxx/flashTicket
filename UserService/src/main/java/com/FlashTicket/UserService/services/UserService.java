package com.FlashTicket.UserService.services;

import com.FlashTicket.UserService.models.domain.UserDomain;
import com.FlashTicket.UserService.models.dtos.UserDTO;
import com.FlashTicket.UserService.models.dtos.UserUpdateDTO;
import com.FlashTicket.UserService.models.entity.User;
import com.FlashTicket.UserService.models.exceptions.UserExistsException;
import com.FlashTicket.UserService.models.exceptions.UserNotFoundException;
import com.FlashTicket.UserService.services.misc.UserMapper;
import com.FlashTicket.UserService.services.misc.UserRepository;
import com.FlashTicket.UserService.services.misc.UserUpdateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final Map<String, UserUpdateStrategy> updateStrategyMap;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, List<UserUpdateStrategy> strategies, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.updateStrategyMap = new HashMap<>();
        this.userRepository = userRepository;

        for (UserUpdateStrategy strategy : strategies) {
            this.updateStrategyMap.putIfAbsent(strategy.getType(), strategy);
        }
    }

    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Unexpected error saving user: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateUser(UserUpdateDTO userUpdateDTO) {
        try {
            UserDomain user = userMapper.entityToDomain(userRepository.findByEmail(userUpdateDTO.email()).orElseThrow(() -> new UserNotFoundException("User [%s] not found".formatted(userUpdateDTO.email()))));
            UserUpdateStrategy strategy = updateStrategyMap.get(userUpdateDTO.type().toLowerCase().trim());
            if (strategy == null) throw new IllegalArgumentException("Unknown type of update");
            strategy.updateUser(user, userUpdateDTO);
        } catch (IllegalArgumentException | UserExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating user [{}]: {}", userUpdateDTO.email(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void upgradeUser(String email) {
        try {
            UserDomain user = userMapper.entityToDomain(userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User [%s] not found".formatted(email))));
            user.upgradeUser();
            userRepository.save(userMapper.domainToEntity(user));
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error upgrading user [{}]: {}", email, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void downgradeUser(String email) {
        try {
            UserDomain user = userMapper.entityToDomain(userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User [%s] not found".formatted(email))));
            user.downgradeUser();
            userRepository.save(userMapper.domainToEntity(user));
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error downgrading user [{}]: {}", email, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User [%s] not found".formatted(email)));
            userRepository.delete(user);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error deleting user [{}]: {}", email, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public UserDTO getUser(String email) {
        try {
            return userMapper.entityToDto(userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User [%s] not found".formatted(email))));
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error getting user [{}]: {}", email, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
