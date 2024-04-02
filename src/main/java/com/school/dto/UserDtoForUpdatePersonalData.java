package com.school.dto;

import com.school.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

import static com.school.service.impl.UserServiceImpl.NAME_NOT_CORRECT;
import static com.school.service.impl.UserServiceImpl.THE_LENGTH_OF_THE_NAME;

public record UserDtoForUpdatePersonalData(
        UUID id,

        @NotBlank(message = NAME_NOT_CORRECT)
        @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
        String firstName,

        @NotBlank(message = NAME_NOT_CORRECT)
        @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
        String lastName,

        @Enumerated(EnumType.STRING)
        User.Status status,

        @Enumerated(EnumType.STRING)
        User.Subject subject,

        @Enumerated(EnumType.STRING)
        User.Level level,

        @Enumerated(EnumType.STRING)
        User.Goal goal,

        String phone,

        String address,

        String aboutYourself
) {
}
