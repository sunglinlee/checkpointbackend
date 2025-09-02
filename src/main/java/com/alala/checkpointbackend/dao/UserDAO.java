package com.alala.checkpointbackend.dao;

import com.alala.checkpointbackend.model.User;
import com.alala.checkpointbackend.model.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Clock;

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

    public void insert(String email) {
        String sql = """
                INSERT INTO USER (
                    EMAIL)
                VALUES (
                    :email)
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

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
                        .build()
        );
    }

    public String getPassword(String email) {
        String sql = "SELECT PASSWORD FROM USER WHERE EMAIL = :email";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

        return jdbcTemplate.queryForObject(sql, parameters, String.class);
    }

    public Integer countByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM USER WHERE EMAIL = :email";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }
}
