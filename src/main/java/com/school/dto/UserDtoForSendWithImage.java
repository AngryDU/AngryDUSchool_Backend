package com.school.dto;

import com.school.entity.User;

import java.util.Optional;
import java.util.UUID;

public record UserDtoForSendWithImage(
        UUID id,
        String email,
        String firstName,
        String lastName,
        User.Status status,
        User.Subject subject,
        User.Level level,
        User.Goal goal,
        String phone,
        String address,
        String aboutYourself,
        AttachmentDtoForSend image
) {
}
