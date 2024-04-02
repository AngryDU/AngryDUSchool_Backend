package com.school.dto;

import com.school.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

import static com.school.service.impl.OrderServiceImpl.ID_NOT_CORRECT;

public record OrderDtoForSave(
        @NotBlank(message = ID_NOT_CORRECT)
        @Pattern(message = "Bad formed student id", //TODO
                regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        UUID studentId,

        @NotBlank(message = ID_NOT_CORRECT)
        @Pattern(message = "Bad formed tutor id", //TODO
                regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        UUID tutorId,

        User.Subject subject
        ) {
}
