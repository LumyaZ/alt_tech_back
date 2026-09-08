package com.shop.producttrialmaster.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemQuantityRequest {

    @NotNull
    @PositiveOrZero
    private Integer quantity;
}