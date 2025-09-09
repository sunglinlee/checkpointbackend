package com.alala.checkpointbackend.service;

import com.alala.checkpointbackend.dao.QuestionnaireDAO;
import com.alala.checkpointbackend.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

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

//    public String query(String email) {
//    }
}
