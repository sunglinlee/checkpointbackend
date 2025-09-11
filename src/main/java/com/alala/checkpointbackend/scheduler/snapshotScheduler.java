package com.alala.checkpointbackend.scheduler;

import com.alala.checkpointbackend.dao.QuestionnaireDAO;
import com.alala.checkpointbackend.dao.UserDAO;
import com.alala.checkpointbackend.model.Questionnaire;
import com.alala.checkpointbackend.model.User;
import com.alala.checkpointbackend.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class snapshotScheduler {
    private static final Logger logger = LoggerFactory.getLogger(snapshotScheduler.class);
    private final QuestionnaireDAO questionnaireDAO;
    private final MailService mailService;
    private final UserDAO userDAO;

    @Scheduled(fixedRate = 60000)
    public void runTaskEveryMinute() throws MessagingException, JsonProcessingException {
        logger.info("排程任務正在執行中...");

        // 1. 取得當前時間的精確瞬間
        Instant now = Instant.now();

        // 2. 減去一分鐘，得到一分鐘前的瞬間
        Instant oneMinuteAgo = now.minus(1, ChronoUnit.MINUTES);

        // 3. 將 Instant 轉換為 java.sql.Timestamp
        Timestamp startTime = Timestamp.from(oneMinuteAgo);
        Timestamp endTime = Timestamp.from(now);

        System.out.println("一分鐘前的時間點: " + startTime);
        System.out.println("當下的時間點: " + endTime);

        // 輸出範例:
        // 一分鐘前的時間點: 2025-09-10 16:13:03.048597
        // 當下的時間點: 2025-09-10 16:14:03.048597

        /*
        輸出範例:
        今天的開始時間 (00:00:00): 2025-09-10 00:00:00.0
        今天的結束時間 (23:59:59): 2025-09-10 23:59:59.999999999
        */

        List<Questionnaire> questionnaires = questionnaireDAO.queryToday(startTime, endTime);
        for (Questionnaire questionnaire : questionnaires) {
            String email = questionnaire.getEmail();
            User user = userDAO.findByEmail(email);

            mailService.sendEmail(email, user, questionnaire);
        }
    }
}

