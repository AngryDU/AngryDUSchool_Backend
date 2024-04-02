package com.school.service.api;

import com.school.dto.UserDtoForUpdatePersonalData;
import jakarta.servlet.http.Cookie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.school.dto.UserDtoForResponse;
import com.school.dto.UserDtoForSave;
import com.school.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getById(UUID id);

    UserDtoForResponse getByEmail(String email);

    Page<User> getAll(Pageable pageable);

    Page<User> getAllTutors(Pageable pageable);

    User create(User user);

    User updatePersonalData(UserDtoForUpdatePersonalData user);

    void delete(UUID id);

    void registerUser(UserDtoForSave dto);

    Cookie loginUser(String username, String password);

    void verify(String username, String code);

    void activateUser(UUID userId);

    void recoveryPassword(String email);

    void changePassword(UUID userId, String newPassword);

    void updatePassword(UUID userId, String oldPassword, String newPassword);

    Page<User> getAllTutorsWithSortParam(String subject, Pageable pageable);

	Page<User> getAllStudentsWithSortParam(String email, 
			String firstName, 
			String lastName, 
			User.Subject subject, 
			User.Level level,
			User.Goal goal,
			String phone,
			String address,
			Pageable pageable);
}
