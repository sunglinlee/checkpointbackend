package com.alala.checkpointbackend.util;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Service
public class DateUtil {
    public Timestamp getCurrentTimePlus8() {
        // 1. 取得 +8 時區的當前時間
        // "Asia/Taipei" 或 "Asia/Shanghai" 是更精確的區域時區 ID
        // "UTC+8" 或 "+08:00" 是固定偏移量的時區 ID
        ZoneId plus8Zone = ZoneId.of("+08:00");
        ZonedDateTime nowInPlus8 = ZonedDateTime.now(plus8Zone);

        System.out.println("當前 +8 時區時間: " + nowInPlus8);

        // 2. 將 ZonedDateTime 轉換為不帶時區的 Instant
        // 這一步很重要，因為 Instant 是以 UTC 為基準的時間戳
        Instant instant = nowInPlus8.toInstant();

        // 3. 將 Instant 轉換為 java.sql.Timestamp
        Timestamp timestamp = Timestamp.from(instant);

        System.out.println("轉換為 Timestamp: " + timestamp);
        return timestamp;
    }

    public Timestamp calculateTime(String scheduleTime, Timestamp currentTime){
        // 將 Timestamp 轉換為 Instant
        Instant instant = currentTime.toInstant();

        // 根據預計發送時間字串，決定要加多少時間單位
        instant = switch (scheduleTime) {
            case "1 分鐘" -> instant.plus(1, ChronoUnit.MINUTES);
            case "1 個月" ->
                // 這裡我們需要更精確的處理，因為每個月的天數不同
                // 為了簡單，我們可以用 30 天來近似
                // 更精準的方法是使用 ZonedDateTime 或 Period，但需要更多程式碼
                    instant.plus(30, ChronoUnit.DAYS);
            case "3 個月" -> instant.plus(90, ChronoUnit.DAYS);
            case "6 個月" -> instant.plus(180, ChronoUnit.DAYS);
            default ->
                // 如果傳入的字串無效，可以拋出例外或返回原始時間
                    throw new IllegalArgumentException("無效的預計發送時間: " + scheduleTime);
        };

        // 將 Instant 轉換回 Timestamp
        return Timestamp.from(instant);
    }
}
