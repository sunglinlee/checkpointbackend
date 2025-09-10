package com.alala.checkpointbackend.service;

import com.alala.checkpointbackend.dao.QuestionnaireDAO;
import com.alala.checkpointbackend.model.Questionnaire;
import com.alala.checkpointbackend.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionnaireService {
    private final QuestionnaireDAO questionnaireDAO;
    private final DateUtil dateUtil;
    private final ObjectMapper objectMapper;

    public String submit(String jsonString) throws JsonProcessingException {
        JsonNode json = objectMapper.readTree(jsonString);
        Timestamp currentTime = dateUtil.getCurrentTimePlus8();
        questionnaireDAO.insert(json, currentTime, dateUtil.calculateTime(json.path("schedule").path("reminder_period").asText(), currentTime));

        return "問卷送出成功";
    }

    public String query(String email) throws JsonProcessingException {
        List<Questionnaire> result = questionnaireDAO.query(email);
        int count = result.size();
        ObjectNode json = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        json.put("total", count);
        json.put("has_more", false);
        json.put("next_offset", 20);

        for (Questionnaire questionnaire : result) {
            ObjectNode json1 = objectMapper.createObjectNode();
            JsonNode moodAndTags = objectMapper.readTree(questionnaire.getMoodAndTags());
            json1.put("id", questionnaire.getEmail() + "_" + questionnaire.getCreateTime());
            json1.put("title", moodAndTags.get("snapshot_title").asText());
            json1.put("date", questionnaire.getCreateTime());
            json1.put("mood", moodAndTags.get("current_mood").asText());
            json1.put("content", moodAndTags.get("current_thoughts").asText());
            json1.put("tags", moodAndTags.get("personal_tags").asText());
            json1.put("schedule_time", questionnaire.getScheduleTime());

            arrayNode.add(json1);
        }
        json.set("questions", arrayNode);
        return json.toString();
    }

    public String querySingle(String snapshotId) throws JsonProcessingException {
        // 1. 分割字串，取得日期部分
        String[] parts = snapshotId.split("_");
        String email = parts[0];
        if (parts.length < 2) {
            System.err.println("字串格式不正確，無法分割。");
        }
        String dateString = parts[1];

        // 2. 定義日期字串的格式
        // 注意格式中的 '.' 後面是毫秒，需要用 'S' 來表示
        // 'S' 是毫秒，可以根據你的精確度使用 'S'、'SS' 或 'SSS'
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        // 3. 解析字串為 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);

        // 4. 轉換為 java.sql.Timestamp
        Timestamp createTime = Timestamp.valueOf(localDateTime);

        System.out.println("原始日期字串: " + dateString);
        System.out.println("轉換後的 Timestamp: " + createTime);


        Questionnaire questionnaire = questionnaireDAO.querySingle(email, createTime);
        ObjectNode json = objectMapper.createObjectNode();
        JsonNode qa = objectMapper.readTree(questionnaire.getQa());
        JsonNode moodAndTags = objectMapper.readTree(questionnaire.getMoodAndTags());
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("title", moodAndTags.get("snapshot_title").asText());
        metadata.put("mood", moodAndTags.get("current_mood").asText());
        metadata.put("tags", moodAndTags.get("personal_tags").asText());
        metadata.put("content", moodAndTags.get("current_thoughts").asText());
        metadata.put("next_reminder", questionnaire.getScheduleTime());

        json.put("id", snapshotId);
        json.put("created_at", questionnaire.getCreateTime());
        json.set("questionnaire_data", qa);
        json.set("metadata", metadata);

        return json.toString();
    }

    public String delete(String snapshotId) {
        String[] parts = snapshotId.split("_");
        String email = parts[0];
        if (parts.length < 2) {
            System.err.println("字串格式不正確，無法分割。");
        }
        String dateString = parts[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        Timestamp createTime = Timestamp.valueOf(localDateTime);

        System.out.println("原始日期字串: " + dateString);
        System.out.println("轉換後的 Timestamp: " + createTime);
        questionnaireDAO.delete(email, createTime);

        return "快照 " + snapshotId + " 已刪除";
    }

    public String update(String snapshotId, String title) throws JsonProcessingException {
        String[] parts = snapshotId.split("_");
        String email = parts[0];
        if (parts.length < 2) {
            System.err.println("字串格式不正確，無法分割。");
        }
        String dateString = parts[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        Timestamp createTime = Timestamp.valueOf(localDateTime);

        System.out.println("原始日期字串: " + dateString);
        System.out.println("轉換後的 Timestamp: " + createTime);
        Questionnaire questionnaire = questionnaireDAO.querySingle(email, createTime);
        String moodAndTags = questionnaire.getMoodAndTags();
        JsonNode moodAndTagsJson = objectMapper.readTree(moodAndTags);
        ((ObjectNode) moodAndTagsJson).put("snapshot_title", title);
        questionnaireDAO.update(email, createTime, moodAndTagsJson.toString());

        ObjectNode json = objectMapper.createObjectNode();
        json.put("id", snapshotId);
        json.put("title", title);
        json.put("updated_at", dateUtil.getCurrentTimePlus8().toString());
        return json.toString();
    }
}
