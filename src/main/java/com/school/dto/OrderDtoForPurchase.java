package com.school.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

import static com.school.service.impl.OrderServiceImpl.ID_NOT_CORRECT;

public record OrderDtoForPurchase(
        @NotBlank(message = ID_NOT_CORRECT)
        @Pattern(message = "Bad formed order id", //TODO
                regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        UUID id,

        @NotBlank(message = "Purchase status cannot be blank")
        @Pattern(message = "Purchase status must be one of the following: PAY, PAID, UNPAID", //TODO
                regexp = "PAY|PAID|UNPAID")
        String purchase
) {
}
