package com.alala.checkpointbackend.model;

public record MailRegisterRequest(String email, String token, String name) {
}
