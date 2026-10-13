package com.FlashTicket.UserService.services.misc;

import com.FlashTicket.UserService.models.domain.UserDomain;
import com.FlashTicket.UserService.models.dtos.UserUpdateDTO;

public interface UserUpdateStrategy {
    String getType();
    void updateUser(UserDomain userDomain, UserUpdateDTO userUpdateDTO);
}
