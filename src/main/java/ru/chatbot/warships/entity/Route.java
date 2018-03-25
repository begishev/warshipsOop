package ru.chatbot.warships.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Route {
    private Integer from;
    private Integer to;
    private Long distance;
    private Long reward;

    public Route(Integer from, Integer to, Long distance, Long reward) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.reward = reward;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getReward() {
        return reward;
    }

    public void setReward(Long reward) {
        this.reward = reward;
    }

    public static class RouteRowMapper implements RowMapper<Route> {
        public RouteRowMapper() {
        }

        public Route mapRow(ResultSet rs, int rowNum) {
            try {
                return new Route(rs.getInt("FROM_PORT"), rs.getInt("TO_PORT"),
                        rs.getLong("DISTANCE"), rs.getLong("REWARD"));
            } catch (SQLException e) {
                return null;
            }
        }
    }
}
