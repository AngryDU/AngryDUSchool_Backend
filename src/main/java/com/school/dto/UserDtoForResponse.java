package com.school.dto;

import com.school.entity.User;

import java.util.UUID;

public record UserDtoForResponse(UUID id,
                                 String email,
                                 String nickName,
                                 User.Status status) {
}
