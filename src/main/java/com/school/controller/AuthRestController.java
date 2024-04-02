package com.school.controller;

import com.school.dto.UserDtoForAuth;
import com.school.dto.UserDtoForRecoveryPass;
import com.school.dto.UserDtoForSave;
import com.school.dto.UserDtoForUpdatePass;
import com.school.security.JwtUtils;
import com.school.service.api.TokenLinkService;
import com.school.service.api.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthRestController implements GlobalController {

    private static final String USER_RECOVERY_SEND_TO_EMAIL = "Token send to Email";
    private static final String USER_UPDATE_SUCCESSFULLY = "Person update password successfully";

    private final UserService userService;
    private final TokenLinkService tokenLinkService;
    private final JwtUtils jwtUtils;
    
    @Value("${app.frontToResetPass}")
    private String resetPassRedirect;
    @Value("${app.frontToLoginPage}")
    private String loginRedirect;

    @Autowired
    public AuthRestController(UserService userService, TokenLinkService tokenLinkService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.tokenLinkService = tokenLinkService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    public void authenticateUser(@Valid @RequestBody UserDtoForAuth dtoForAuth,
                                 HttpServletResponse response) {
        Cookie cookie = userService.loginUser(dtoForAuth.email(), dtoForAuth.password());
        response.addCookie(cookie);
    }

    @PostMapping("/signout")
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie cookie = jwtUtils.getCleanJwtCookie();
        response.addCookie(cookie);
    }

    @PostMapping("/registration")
    public ResponseEntity<RedirectView> registration(@Valid @RequestBody UserDtoForSave user) {
        userService.registerUser(user);
        RedirectView redirectView = new RedirectView(loginRedirect);
        return new ResponseEntity<>(redirectView, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/activate/{token}/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable String token, @PathVariable UUID userId) {
        tokenLinkService.activate(token);
        userService.activateUser(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(loginRedirect));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/recoveryPass")
    public ResponseEntity<?> performRecoveryPass(@Valid @RequestBody UserDtoForRecoveryPass user) {
        userService.recoveryPassword(user.email());
        return new ResponseEntity<>(USER_RECOVERY_SEND_TO_EMAIL, HttpStatus.OK);
    }

    @GetMapping("/recoveryPass/{token}/{userId}")
    public ResponseEntity<?> recoveryPass(@PathVariable String token, @PathVariable Long userId) {
        tokenLinkService.activate(token);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(resetPassRedirect + userId));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserDtoForUpdatePass user) {
        userService.changePassword(user.id(), user.newPassword());
        return new ResponseEntity<>(USER_UPDATE_SUCCESSFULLY, HttpStatus.OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UserDtoForUpdatePass user) {
        userService.updatePassword(user.id(), user.oldPassword(), user.newPassword());
        return new ResponseEntity<>(USER_UPDATE_SUCCESSFULLY, HttpStatus.OK);
    }
}