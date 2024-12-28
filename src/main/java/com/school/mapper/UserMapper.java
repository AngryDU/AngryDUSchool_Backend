package com.school.mapper;

import com.school.dto.AttachmentDtoForSend;
import com.school.dto.UserDtoForResponse;
import com.school.dto.UserDtoForSave;
import com.school.dto.UserDtoForSendWithImage;
import com.school.dto.UserDtoForUpdatePersonalData;
import com.school.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User userToUserDtoForSaveToUser(UserDtoForSave dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setStatus(dto.status());
        return user;
    }

    public User userToUserDtoForUpdatePersonalDataToUser(UserDtoForUpdatePersonalData dto) {
        User user = new User();
        user.setId(dto.id());
        user.setLevel(dto.level());
        user.setAddress(dto.address());
        user.setAboutYourself(dto.aboutYourself());
        user.setStatus(dto.status());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhone(dto.phone());
        user.setSubject(dto.subject());
        user.setGoal(dto.goal());
        return user;
    }

    public UserDtoForResponse userDtoToUserDtoForResponse(User user) {
        return new UserDtoForResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getStatus()
        );
    }

    public UserDtoForSendWithImage userToUserDtoForSendWithImage(User user, AttachmentDtoForSend image) {
        UserDtoForSendWithImage dto = new UserDtoForSendWithImage(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus(),
                user.getSubject(),
                user.getLevel(),
                user.getGoal(),
                user.getPhone(),
                user.getAddress(),
                user.getAboutYourself(),
                image
        );
        return dto;
    }
}
