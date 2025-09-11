package com.alala.checkpointbackend.dao;

import com.alala.checkpointbackend.model.Questionnaire;
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

    public void insert(JsonNode request, Timestamp createTime, Timestamp scheduleTime, String period) {
        String sql = """
                INSERT INTO QUESTIONNAIRE (
                    EMAIL, QA, CREATE_TIME, SCHEDULE_TIME, MOOD_AND_TAGS, PERIOD)
                VALUES (
                    :email, :qa, :createTime, :scheduleTime, :moodAndTags, :period)
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", request.path("email").asText())
                .addValue("qa", request.get("qa").toString())
                .addValue("createTime", createTime)
                .addValue("scheduleTime", scheduleTime)
                .addValue("moodAndTags", request.get("mood_and_tags").toString())
                .addValue("period", period);

        jdbcTemplate.update(sql, parameters);
    }

    public List<Questionnaire> query(String email) {
        String sql = """
                SELECT * FROM QUESTIONNAIRE WHERE EMAIL = :email
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

        return jdbcTemplate.query(sql, parameters, (rs, rowNum) ->
                Questionnaire.builder()
                        .email(rs.getString("EMAIL"))
                        .qa(rs.getString("QA"))
                        .createTime(rs.getTimestamp("CREATE_TIME").toString())
                        .scheduleTime(rs.getTimestamp("SCHEDULE_TIME").toString())
                        .moodAndTags(rs.getString("MOOD_AND_TAGS"))
                        .period(rs.getString("PERIOD"))
                        .build()
        );
    }

    public List<Questionnaire> queryToday(Timestamp startTime, Timestamp endTime) {
        String sql = """
                SELECT * FROM QUESTIONNAIRE WHERE SCHEDULE_TIME BETWEEN :startTime AND :endTime
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("startTime", startTime)
                .addValue("endTime", endTime);

        return jdbcTemplate.query(sql, parameters, (rs, rowNum) ->
                Questionnaire.builder()
                        .email(rs.getString("EMAIL"))
                        .qa(rs.getString("QA"))
                        .createTime(rs.getTimestamp("CREATE_TIME").toString())
                        .scheduleTime(rs.getTimestamp("SCHEDULE_TIME").toString())
                        .moodAndTags(rs.getString("MOOD_AND_TAGS"))
                        .period(rs.getString("PERIOD"))
                        .build()
        );
    }

    public Questionnaire querySingle(String email, Timestamp createTime) {
        String sql = """
                SELECT * FROM QUESTIONNAIRE WHERE EMAIL = :email AND CREATE_TIME = :createTime
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("createTime", createTime);

        return jdbcTemplate.queryForObject(sql, parameters, (rs, rowNum) ->
                Questionnaire.builder()
                        .email(rs.getString("EMAIL"))
                        .qa(rs.getString("QA"))
                        .createTime(rs.getTimestamp("CREATE_TIME").toString())
                        .scheduleTime(rs.getTimestamp("SCHEDULE_TIME").toString())
                        .moodAndTags(rs.getString("MOOD_AND_TAGS"))
                        .period(rs.getString("PERIOD"))
                        .build()
        );
    }

    public void delete(String email, Timestamp createTime) {
        String sql = """
                DELETE FROM QUESTIONNAIRE WHERE EMAIL = :email AND CREATE_TIME = :createTime
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("createTime", createTime);

        jdbcTemplate.update(sql, parameters);
    }

    public void update(String email, Timestamp createTime, String title) {
        String sql = """
                UPDATE QUESTIONNAIRE SET MOOD_AND_TAGS = :title WHERE EMAIL = :email AND CREATE_TIME = :createTime
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email",email)
                .addValue("createTime", createTime)
                .addValue("title", title);

        jdbcTemplate.update(sql, parameters);
    }
}
