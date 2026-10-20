package com.FlashTicket.InventoryService.models.domain.valueObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record EventPrice(BigDecimal price) {
    public EventPrice {
        Objects.requireNonNull(price, "price cant be null");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Price cant be negative");
        price = price.setScale(2, RoundingMode.HALF_UP);
    }
}
