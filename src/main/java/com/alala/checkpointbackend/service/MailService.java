package com.alala.checkpointbackend.service;

import com.alala.checkpointbackend.model.Questionnaire;
import com.alala.checkpointbackend.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    private String username;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;

    public void sendForgetPasswordMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username); // 寄件人
        message.setTo(to); // 收件人
        message.setSubject(subject); // 郵件主題
        message.setText(body); // 郵件內容

        mailSender.send(message);
    }

    public void sendEmail(String to, User user, Questionnaire questionnaire) throws MessagingException, JsonProcessingException {
        JsonNode moodAndTags = objectMapper.readTree(questionnaire.getMoodAndTags());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        // 建立 Thymeleaf Context，並設定動態變數
        Context context = new Context();
        context.setVariable("recipientName", user.getName());
        context.setVariable("duration", questionnaire.getPeriod());
        context.setVariable("userMessage", moodAndTags.get("current_thoughts").asText());
        context.setVariable("reviewUrl", "URL");

        // 使用樣板引擎處理 HTML 樣板
        String htmlContent = templateEngine.process("time-capsule-email", context);

        // 設定郵件主旨、預覽文字和內容
        helper.setSubject("嘿 " + user.getName() + "，一封來自" + questionnaire.getPeriod() + "前自己的信，請查收 💌");
        helper.setText(htmlContent, true); // 第二個參數設為 true 表示內容為 HTML
        helper.setFrom(username); // 替換為你的發送 Email

        // 設定收件人
        helper.setTo(to);

        // 發送郵件
        mailSender.send(message);
    }
}
