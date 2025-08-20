package com.alala.checkpointbackend.dao;

import com.alala.checkpointbackend.model.MailRegisterRequest;
import com.alala.checkpointbackend.model.User;
import com.alala.checkpointbackend.model.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.Instant;

@Repository
@RequiredArgsConstructor
public class UserDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Clock clock;

    public void insert(UserRegisterRequest request) {
        String sql = """
                INSERT INTO USER (
                    EMAIL, PASSWORD, NAME)
                VALUES (
                    :email, :password, :name)
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", request.email())
                .addValue("password", request.password())
                .addValue("name", request.name());

        jdbcTemplate.update(sql, parameters);
    }

    public void insert(MailRegisterRequest request) {
        String sql = """
                INSERT INTO USER (
                    EMAIL, NAME, CREATED_TIME, UPDATED_TIME)
                VALUES (
                    :email, :name, :createdTime, :updateTime)
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", request.email())
                .addValue("nickname", request.name())
                .addValue("createdTime", Instant.now(clock).toEpochMilli())
                .addValue("updateTime", Instant.now(clock).toEpochMilli());

        jdbcTemplate.update(sql, parameters);
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM USER WHERE EMAIL = :email";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

        return jdbcTemplate.queryForObject(sql, parameters, (rs, rowNum) ->
                User.builder()
                        .email(rs.getString("EMAIL"))
                        .name(rs.getString("NAME"))
                        .password(rs.getString("PASSWORD"))
                        .build()
        );
    }

    public int countByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM USER WHERE EMAIL = :email";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }
}
