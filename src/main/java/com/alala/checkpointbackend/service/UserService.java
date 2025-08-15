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
    private final RedisService redisService;

    public String register(UserRegisterRequest request) {
        userDAO.insert(request);
        redisService.set(request.email(), UUID.randomUUID().toString());
        return userDAO.findByEmail(request.email()).toString();
    }

    public String mailRegister(MailRegisterRequest request) {
        if (userDAO.countByEmail(request.email()) == 0) {
            userDAO.insert(request);
        }
        redisService.set(request.email(), request.token());
        return userDAO.findByEmail(request.email()).toString();
    }

    public String mailLogin(MailLoginRequest request) {
        redisService.set(request.email(), request.token());
        return userDAO.findByEmail(request.email()).toString();
    }

    public String login(UserLoginRequest request) {
        User user = userDAO.findByEmail(request.email());
        if (user == null || !user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        redisService.set(request.email(), UUID.randomUUID().toString());
        return user.toString();
    }

    public String logout(UserLoginRequest request) {
        if (redisService.get(request.email())==null){
            throw new IllegalArgumentException("User not logged in");
        }
        redisService.delete(request.email());
        return "User logged out successfully";
    }

    public String refreshToken(String email) {
        String token = redisService.get(email);
        if (token == null) {
            throw new IllegalArgumentException("User not logged in");
        }
        redisService.set(email, token);
        return "Token refreshed successfully";
    }
}
