package com.FlashTicket.UserService.models.domain.valueObjects;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.Objects;
import java.util.UUID;

public record UserId (UUID value) {
    public UserId {
        Objects.requireNonNull(value, "user id cant be null");
    }

    public static UserId generateUserId() {
        return new UserId(UuidCreator.getTimeOrderedEpoch());
    }
}
