package com.alala.checkpointbackend.dao;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionnaireDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void insert(JsonNode request, Timestamp createTime, Timestamp scheduleTime) {
        String sql = """
                INSERT INTO QUESTIONNAIRE (
                    EMAIL, QA, CREATE_TIME, SCHEDULE_TIME, MOOD_AND_TAGS)
                VALUES (
                    :email, :qa, :createTime, :scheduleTime, :moodAndTags)
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", request.path("email").toString())
                .addValue("qa", request.get("qa").asText())
                .addValue("createTime", createTime)
                .addValue("scheduleTime", scheduleTime)
                .addValue("moodAndTags",request.get("mood_and_tags").toString());

        jdbcTemplate.update(sql, parameters);
    }

//    public List<> insert(JsonNode request, Timestamp createTime, Timestamp scheduleTime) {
//        String sql = """
//                INSERT INTO QUESTIONNAIRE (
//                    EMAIL, QA, CREATE_TIME, SHEDULE_TIME, MOOD_AND_TAGS)
//                VALUES (
//                    :email, :qa, :createTime, :scheduleTime, :moodAndTags)
//                """;
//
//        MapSqlParameterSource parameters = new MapSqlParameterSource()
//                .addValue("email", request.path("email").asText())
//                .addValue("qa", request.path("qa").asText())
//                .addValue("createTime", createTime)
//                .addValue("scheduleTime", scheduleTime)
//                .addValue("moodAndTags",request.path("mood_and_tags").asText());
//
//        jdbcTemplate.update(sql, parameters);
//    }
}
