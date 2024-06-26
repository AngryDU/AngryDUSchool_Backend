package com.school.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.dto.UserDtoForResponse;
import com.school.dto.UserDtoForSave;
import com.school.dto.UserDtoForUpdatePersonalData;
import com.school.entity.SecurityUser;
import com.school.entity.User;
import com.school.exception.CustomException;
import com.school.exception.ExceptionLocations;
import com.school.mapper.UserMapper;
import com.school.message.InternalizationMessageManagerConfig;
import com.school.repository.UserRepository;
import com.school.security.JwtUtils;
import com.school.service.api.MailService;
import com.school.service.api.TokenLinkService;
import com.school.service.api.UserService;

import jakarta.servlet.http.Cookie;

@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    public static final String EMAIL_NOT_CORRECT = "UserService.NotCorrectEmail";
    public static final String PASSWORD_NOT_CORRECT = "UserService.NotCorrectPassword";
    public static final String NAME_NOT_CORRECT = "UserService.NotCorrectName";
    public static final String THE_LENGTH_OF_THE_NAME = "The length of the name must be from 2 to 255 characters";
    public static final String KET_FOR_EMAIL_RECOVERY_PASSWORD_SUBJECT = "UserService.EmailRecoveryPasswordSubject";
    private static final int REGISTER_TOKEN_ACTIVITY_SECONDS = 60 * 60;
    private static final int RECOVERY_TOKEN_ACTIVITY_SECONDS = 5 * 60;
    private static final int COOKIE_TOKEN_ACTIVITY_SECONDS = 24 * 60 * 60;
    public static final String KEY_FOR_EXCEPTION_USER_NOT_FOUND = "UserService.UserNotFound";
    public static final String KEY_FOR_EXCEPTION_INCORRECT_SORT_PARAM = "UserService.IncorrectSortParam";
    public static final String KEY_FOR_EXCEPTION_EXISTING_EMAIL = "UserService.ExistingEmail";
    public static final String KEY_FOR_EXCEPTION_ACTIVATE_LINK_PATTERN = "UserService.ActivateLinkPattern";
    public static final String KEY_FOR_EMAIL_USER_CONFIRMATION_SUBJECT = "UserService.UserConfirmationSubject";
    public static final String KEY_FOR_RECOVERY_PASS_LINK_PATTERN = "UserService.RecoveryPassLinkPattern";
    public static final String KEY_FOR_EXCEPTION_WRONG_OLD_PASSWORD = "UserService.WrongOldPassword";
    public static final String KEY_FOR_EXCEPTION_USER_NOT_ACTIVATED = "UserService.UserNotActivated";
    public static final String THERE_IS_NO_ENUM = "UserService.ThereIsNoEnum";
    public static final String STATUS_NOT_SPECIFIED = "UserService.StatusNotSpecified";
    public static final int LOGIN_TOKEN_ACTIVITY_SECONDS = 24 * 60 * 60;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenLinkService tokenLinkService;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Value("${app.host}")
    private String host;
    @Value("${server.servlet.contextPath}")
    private String contextPath;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           TokenLinkService tokenLinkService,
                           MailService mailService,
                           @Lazy AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenLinkService = tokenLinkService;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User getById(UUID id) {
        User newUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND));

        return newUser;
    }

    @Override
    public UserDtoForResponse getByEmail(String email) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND));

        UserDtoForResponse userForResponse = userMapper.userDtoToUserDtoForResponse(existedUser);

        return userForResponse;
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return users;
    }

    @Override
    public Page<User> getAllTutors(Pageable pageable) {
        Page<User> tutors = userRepository.findAllTutors(pageable);

        return tutors;
    }

    @Override
    public Page<User> getAllTutorsWithSortParam(String sortParam, Pageable pageable) {
        Page<User> sortedTutors;

        if (enumClassesCompare(User.Subject.class, sortParam)) {
            sortedTutors = userRepository.findAllTutorsWithSubject(User.Subject.valueOf(sortParam), pageable);

        } else if (enumClassesCompare(User.Goal.class, sortParam)) {
            sortedTutors = userRepository.findAllTutorsWithGoal(User.Goal.valueOf(sortParam), pageable);

        } else if (enumClassesCompare(User.Level.class, sortParam)) {
            sortedTutors = userRepository.findAllTutorsWithLevel(User.Level.valueOf(sortParam), pageable);

        } else {
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(THERE_IS_NO_ENUM),
                    ExceptionLocations.USER_SERVICE_VALIDATION);
        }

        return sortedTutors;
    }

    private <T extends Enum<T>> boolean enumClassesCompare(Class<T> comparedEnum, String comparedString) {
        try {
            Enum.valueOf(comparedEnum, comparedString);

            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
	public Page<User> getAllStudentsWithSortParam(String email, 
			String firstName, 
			String lastName, 
			User.Subject subject, 
			User.Level level,
			User.Goal goal,
			String phone,
			String address,
			Pageable pageable) {
    	Page<User> sortedUsers = userRepository.findAllStudentsWithSortParam(email,
    			firstName, 
    			lastName, 
    			subject, 
    			level, 
    			goal, 
    			phone, 
    			address, 
    			pageable);
		return sortedUsers;
	}

	@Override
    public User create(User user) {
        User entity = user;

        entity.setActive(true);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        entity.setPassword(encodedPassword);
        entity.setEmail(user.getEmail().trim());

        User newUser = userRepository.save(entity);

        return newUser;
    }

    @Override
    public User updatePersonalData(UserDtoForUpdatePersonalData dto) {
        Optional<User> existingOptional = userRepository.findByIdActive(dto.id());

        User existing = existingOptional.orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                ExceptionLocations.USER_SERVICE_NOT_FOUND));

        User user = userMapper.userDtoForUpdatePersonalDataToUser(dto);
        user.setId(existing.getId());
        user.setEmail(existing.getEmail());
        user.setPassword(existing.getPassword());
        user.setActive(true);

        User updatedUser = userRepository.save(user);

        return updatedUser;
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND));

        if (!user.isActive()) {
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                    ExceptionLocations.USER_SERVICE_NOT_FOUND);
        }

        user.setActive(false);

        userRepository.save(user);
    }

    @Override
    public void registerUser(UserDtoForSave dtoForSave) {
        Optional<User> existing = userRepository.findByEmail(dtoForSave.email());

        if (existing.isPresent()) {
            throw new CustomException(String.format(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_EXCEPTION_EXISTING_EMAIL), dtoForSave.email()),
                    ExceptionLocations.USER_SERVICE_VALIDATION);
        }

        User entity = userMapper.userDtoForSaveToUser(dtoForSave);

        String encodedPassword = passwordEncoder.encode(dtoForSave.password());
        entity.setPassword(encodedPassword);
        entity.setEmail(dtoForSave.email().trim());
        entity.setFirstName(dtoForSave.firstName());
        entity.setLastName(dtoForSave.lastName());
        entity.setActive(false);

        User created = userRepository.save(entity);

        String token = tokenLinkService.generateToken(REGISTER_TOKEN_ACTIVITY_SECONDS, created.getFirstName());
        mailService.sendEmail(created.getEmail(), InternalizationMessageManagerConfig
                        .getMessage(KEY_FOR_EMAIL_USER_CONFIRMATION_SUBJECT),
                String.format(InternalizationMessageManagerConfig
                        .getMessage(KEY_FOR_EXCEPTION_ACTIVATE_LINK_PATTERN), host, contextPath, token, created.getId()));
    }

    @Override
    public Cookie loginUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(InternalizationMessageManagerConfig
                .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                ExceptionLocations.USER_SERVICE_NOT_FOUND));

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        String token = tokenLinkService.generateToken(authentication, LOGIN_TOKEN_ACTIVITY_SECONDS);

        Cookie cookie = jwtUtils.createCookieWithJwt(token, COOKIE_TOKEN_ACTIVITY_SECONDS);

        return cookie;
    }

    @Override
    public void verify(String username, String code) {
        userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND));
    }

    @Override
    public void activateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND));

        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void recoveryPassword(String email) {
        User existing = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND));

        String token = tokenLinkService.generateToken(RECOVERY_TOKEN_ACTIVITY_SECONDS, existing.getFirstName());

        mailService.sendEmail(email, InternalizationMessageManagerConfig
                        .getMessage(KET_FOR_EMAIL_RECOVERY_PASSWORD_SUBJECT),
                String.format(InternalizationMessageManagerConfig
                        .getMessage(KEY_FOR_RECOVERY_PASS_LINK_PATTERN), host, contextPath, token, existing.getId()));
    }

    @Override
    public void changePassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UUID userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_FOUND),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_EXCEPTION_WRONG_OLD_PASSWORD),
                    ExceptionLocations.USER_SERVICE_VALIDATION);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailActive(email)
                .map(SecurityUser::new)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_USER_NOT_ACTIVATED),
                        ExceptionLocations.USER_SERVICE_VALIDATION));
    }
}
