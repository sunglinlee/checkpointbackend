package com.alala.checkpointbackend.controller;

import com.alala.checkpointbackend.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final MailService emailService;

    @GetMapping("/send-test-email")
    public String sendTestEmail() {
        String recipient = "mdlss325@gmail.com";
        String subject = "這是一封測試郵件";
        String body = "恭喜！你成功使用 Spring Boot 寄出了第一封郵件。";

        emailService.sendEmail(recipient, subject, body);

        return "郵件已成功發送！";
    }
}
