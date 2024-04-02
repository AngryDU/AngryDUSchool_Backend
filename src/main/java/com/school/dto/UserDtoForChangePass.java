package com.school.dto;

import lombok.Data;

@Data
public class UserDtoForChangePass {
    private Long id;
    private String newPassword;
}
