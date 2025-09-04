package com.alala.checkpointbackend.model;

public record UserChangePasswordRequest(String email, String currentPassword, String newPassword) {
}
