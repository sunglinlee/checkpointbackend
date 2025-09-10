package com.alala.checkpointbackend.service;

import com.alala.checkpointbackend.dao.UserDAO;
import com.alala.checkpointbackend.exception.DuplicateUserException;
import com.alala.checkpointbackend.exception.UserNotLoginException;
import com.alala.checkpointbackend.exception.WrongPasswordException;
import com.alala.checkpointbackend.model.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final CacheService cacheService;
    private final MailService mailService;

    public User register(UserRegisterRequest request) throws DuplicateUserException {
        try {
            userDAO.insert(request);
//            cacheService.put(request.email(), UUID.randomUUID().toString(), 3600L);
        } catch (Exception e) {
            throw new DuplicateUserException("Email already in use");
        }
        return userDAO.findByEmail(request.email());
    }

    public User mailLogin(MailLoginRequest request) throws DuplicateUserException {
        try {
//            cacheService.put(request.email(), request.googleId(), 3600L);
            if (userDAO.countByEmail(request.email()) == 0) {
                userDAO.insert(request.email(), request.name());
            }
        } catch (Exception e) {
            throw new DuplicateUserException("Email already in use");
        }
        return userDAO.findByEmail(request.email());
    }

    public User login(UserLoginRequest request) throws WrongPasswordException {
        String password = userDAO.getPassword(request.email());
        if (password == null || !password.equals(request.password())) {
            throw new WrongPasswordException("Invalid email or password");
        }
//        cacheService.put(request.email(), UUID.randomUUID().toString(), 3600L);
        return userDAO.findByEmail(request.email());
    }

    public String logout(UserLoginRequest request) throws UserNotLoginException {
//        if (cacheService.get(request.email()) == null) {
//            throw new UserNotLoginException("User not logged in");
//        }
        cacheService.remove(request.email());
        return "User logged out successfully";
    }

    public String refreshToken(String email) {
//        String token = (String) cacheService.get(email);
//        if (token == null) {
//            throw new IllegalArgumentException("User not logged in");
//        }
//        cacheService.put(email, token, 3600L);
        return "Token refreshed successfully";
    }

    public User changeName(UserChangeNameRequest request) throws UserNotLoginException {
//        String token = (String) cacheService.get(request.email());
//        if (token == null) {
//            throw new UserNotLoginException("User not logged in");
//        }
        userDAO.updateName(request.email(), request.name());
        return userDAO.findByEmail(request.email());
    }

    public String changePassword(UserChangePasswordRequest request) throws UserNotLoginException, WrongPasswordException {
//        String token = (String) cacheService.get(request.email());
//        if (token == null) {
//            throw new UserNotLoginException("User not logged in");
//        }
        String password = userDAO.getPassword(request.email());
        if (password == null || !password.equals(request.currentPassword())) {
            throw new WrongPasswordException("Invalid email or password");
        }
        userDAO.updatePassword(request.email(), request.newPassword());
        return "Password changed successfully";
    }

    public String forgetPassword(String email) throws MessagingException {
        String password = userDAO.getPassword(email);
        mailService.sendForgetPasswordMail(email, "忘記密碼", "484智障密碼都記不住？ 您的密碼為: " + password);
        return "Password changed successfully";
    }
}
