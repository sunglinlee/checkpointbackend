package com.alala.checkpointbackend.controller;

import com.alala.checkpointbackend.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final MailService emailService;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @GetMapping("/send-test-email")
    public String sendTestEmail() throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        String recipient = "mdlss325@gmail.com";


        // 建立 Thymeleaf Context，並設定動態變數
        Context context = new Context();
        context.setVariable("recipientName", "123");
        context.setVariable("duration", "三個月");
        context.setVariable("userMessage", "這是使用者的留言");
        context.setVariable("reviewUrl", "URL");

        // 使用樣板引擎處理 HTML 樣板
        String htmlContent = templateEngine.process("time-capsule-email", context);

        // 設定郵件主旨、預覽文字和內容
        helper.setSubject("嘿 " + "123" + "，一封來自" + "三個月" + "前自己的信，請查收 💌");
        helper.setText(htmlContent, true); // 第二個參數設為 true 表示內容為 HTML
        helper.setFrom("your_email@gmail.com"); // 替換為你的發送 Email

        // 設定收件人
        helper.setTo(recipient);

        // 發送郵件
        mailSender.send(message);


        return "郵件已成功發送！";
    }
}
