package com.alala.checkpointbackend.service;

import com.alala.checkpointbackend.dao.UserDAO;
import com.alala.checkpointbackend.exception.DuplicateUserException;
import com.alala.checkpointbackend.exception.WrongPasswordException;
import com.alala.checkpointbackend.model.MailLoginRequest;
import com.alala.checkpointbackend.model.User;
import com.alala.checkpointbackend.model.UserLoginRequest;
import com.alala.checkpointbackend.model.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final CacheService cacheService;

    public User register(UserRegisterRequest request) throws DuplicateUserException {
        try {
            userDAO.insert(request);
            cacheService.put(request.email(), UUID.randomUUID().toString(), 3600L);
        } catch (Exception e) {
            throw new DuplicateUserException("Email already in use");
        }
        return userDAO.findByEmail(request.email());
    }

    public User mailLogin(MailLoginRequest request) {
        cacheService.put(request.email(), request.token(), 3600L);
        if (userDAO.countByEmail(request.email()) == 0) {
            userDAO.insert(request.email());
        }
        return userDAO.findByEmail(request.email());
    }

    public User login(UserLoginRequest request) throws WrongPasswordException {
        String password = userDAO.getPassword(request.email());
        if (password == null || !password.equals(request.password())) {
            throw new WrongPasswordException("Invalid email or password");
        }
        cacheService.put(request.email(), UUID.randomUUID().toString(), 3600L);
        return userDAO.findByEmail(request.email());
    }

    public String logout(UserLoginRequest request) {
        if (cacheService.get(request.email()) == null) {
            throw new IllegalArgumentException("User not logged in");
        }
        cacheService.remove(request.email());
        return "User logged out successfully";
    }

    public String refreshToken(String email) {
        String token = (String) cacheService.get(email);
        if (token == null) {
            throw new IllegalArgumentException("User not logged in");
        }
        cacheService.put(email, token, 3600L);
        return "Token refreshed successfully";
    }
}
