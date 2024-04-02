package com.school.mapper;

import com.school.dto.UserDtoForUpdatePersonalData;
import org.springframework.stereotype.Component;
import com.school.dto.UserDtoForResponse;
import com.school.dto.UserDtoForSave;
import com.school.entity.User;

@Component
public class UserMapper {
    public User userDtoForSaveToUser(UserDtoForSave dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setStatus(dto.status());
        return user;
    }
    public User userDtoForUpdatePersonalDataToUser(UserDtoForUpdatePersonalData dto) {
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
}
