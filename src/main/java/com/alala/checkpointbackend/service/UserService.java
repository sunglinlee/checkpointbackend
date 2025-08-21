package com.alala.checkpointbackend.service;

import com.alala.checkpointbackend.dao.UserDAO;
import com.alala.checkpointbackend.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final CacheService cacheService;

    public String register(UserRegisterRequest request) {
        userDAO.insert(request);
        cacheService.put(request.email(), UUID.randomUUID().toString(), 3600L);
        return userDAO.findByEmail(request.email()).toString();
    }

    public String mailRegister(MailRegisterRequest request) {
        if (userDAO.countByEmail(request.email()) == 0) {
            userDAO.insert(request);
        }
        cacheService.put(request.email(), request.token(), 3600L);
        return userDAO.findByEmail(request.email()).toString();
    }

    public String mailLogin(MailLoginRequest request) {
        cacheService.put(request.email(), request.token(), 3600L);
        return userDAO.findByEmail(request.email()).toString();
    }

    public String login(UserLoginRequest request) {
        User user = userDAO.findByEmail(request.email());
        if (user == null || !user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        cacheService.put(request.email(), UUID.randomUUID().toString(), 3600L);
        return user.toString();
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
