package com.school.controller;

import com.school.dto.UserDtoForUpdatePersonalData;
import com.school.entity.User;
import com.school.service.api.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserRestController implements GlobalController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping
    public Page<User> getAll(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping("/tutors")
    public Page<User> getAllTutors(Pageable pageable) {
        return userService.getAllTutors(pageable);
    }

    @GetMapping("/tutors/{sortParam}")
    public Page<User> getAllTutorsWithSortParam(@PathVariable String sortParam, Pageable pageable) {
        return userService.getAllTutorsWithSortParam(sortParam, pageable);
    }

    @GetMapping("/students")
    public Page<User> getAllStudentsWithSortParam(@RequestParam(required = false) String email,
    		@RequestParam(required = false) String firstName,
    		@RequestParam(required = false) String lastName,
    		@RequestParam(required = false) User.Subject subject,
    		@RequestParam(required = false) User.Level level,
    		@RequestParam(required = false) User.Goal goal,
    		@RequestParam(required = false) String phone,
    		@RequestParam(required = false) String address,
    		Pageable pageable) {
    	return userService.getAllStudentsWithSortParam(email, firstName, lastName, subject, level, goal, phone, address, pageable);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @PostMapping("/personal_data")
    public User updatePersonalData(@RequestBody UserDtoForUpdatePersonalData user) {
        return userService.updatePersonalData(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}
