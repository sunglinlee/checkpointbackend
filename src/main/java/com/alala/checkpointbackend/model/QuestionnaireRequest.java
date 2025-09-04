package com.alala.checkpointbackend.model;

public record UserRegisterRequest(String email, String password, String name) {
}
