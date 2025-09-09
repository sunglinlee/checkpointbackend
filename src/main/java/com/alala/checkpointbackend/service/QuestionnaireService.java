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
        json.put("has_more",false);
        json.put("next_offset", 20);

        for (Questionnaire questionnaire : result) {
            ObjectNode json1 = objectMapper.createObjectNode();
            JsonNode moodAndTags = objectMapper.readTree(questionnaire.getMoodAndTags());
            json1.put("id", questionnaire.getEmail()+"_"+questionnaire.getCreateTime());
            json1.put("title", moodAndTags.get("snapshot_title").asText());
            json1.put("date", questionnaire.getCreateTime());
            json1.put("mood", moodAndTags.get("current_mood").asText());
            json1.put("content", moodAndTags.get("current_thoughts").asText());
            json1.put("tags", moodAndTags.get("personal_tags").asText());

            arrayNode.add(json1);
        }
        json.set("questions", arrayNode);
        return json.toString();
    }
}
