package ru.chatbot.warships.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Port {
    Integer id;
    String name;
    Integer x;
    Integer y;
    Team owner;
    Integer distance;
    Integer reward;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Team getOwner() {
        return owner;
    }

    public void setOwner(Team owner) {
        this.owner = owner;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public Port(Integer id, String name, Integer x, Integer y, Team owner) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    public Port(Integer id, String name, Integer x, Integer y, Team owner, Integer distance, Integer reward) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.distance = distance;
        this.reward = reward;
    }

    public static class PortRowMapper implements RowMapper<Port> {
        public PortRowMapper() {
        }

        public Port mapRow(ResultSet rs, int rowNum) {
            try {
                return new Port(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("X"),
                        rs.getInt("Y"), Team.valueOf(rs.getInt("OWNER")));
            } catch (SQLException e) {
                return null;
            }
        }
    }

    public static class PortWithDistanceRowMapper implements RowMapper<Port> {
        public PortWithDistanceRowMapper() {
        }

        public Port mapRow(ResultSet rs, int rowNum) {
            try {
                return new Port(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("X"),
                        rs.getInt("Y"), Team.valueOf(rs.getInt("OWNER")),
                        rs.getInt("DISTANCE"), rs.getInt("REWARD"));
            } catch (SQLException e) {
                return null;
            }
        }
    }
}
