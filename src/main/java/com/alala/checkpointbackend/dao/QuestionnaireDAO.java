package com.alala.checkpointbackend.dao;

import com.alala.checkpointbackend.model.QuestionnaireRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
@RequiredArgsConstructor
public class QuestionnaireDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void insert(QuestionnaireRequest request, Timestamp createTime, Timestamp scheduleTime) {
        String sql = """
                INSERT INTO QUESTIONNAIRE (
                    EMAIL, QA, CREATE_TIME, SHEDULE_TIME)
                VALUES (
                    :email, :qa, :createTime, :scheduleTime)
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", request.email())
                .addValue("qa", request.qa())
                .addValue("createTime", createTime)
                .addValue("scheduleTime", scheduleTime);

        jdbcTemplate.update(sql, parameters);
    }
}
