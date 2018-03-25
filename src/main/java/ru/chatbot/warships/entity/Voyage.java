package ru.chatbot.warships.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Voyage {
    private Integer playerId;
    private Integer leaderId;
    private Integer destination;
    private Timestamp startTime;
    private Timestamp finishTime;
    private Integer finished;
    private Integer type; // TODO:should be VoyageType
    private Integer reward;

    // for travel
    public Voyage(Integer playerId, Integer destination, Timestamp startTime, Timestamp finishTime) {
        this.playerId = playerId;
        this.destination = destination;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    // for trade
    public Voyage(Integer playerId, Integer leaderId, Integer destination, Timestamp startTime, Timestamp finishTime, Integer reward) {
        this.playerId = playerId;
        this.leaderId = leaderId;
        this.destination = destination;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.reward = reward;
    }

    Voyage(Integer playerId, Integer leaderId, Timestamp startTime, Timestamp finishTime, Integer finished, Integer type, Integer reward) {
        this.playerId = playerId;
        this.leaderId = leaderId;
        this.finishTime = finishTime;
        this.startTime = startTime;
        this.finished = finished;
        this.type = type;
        this.reward = reward;
    }
    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }



    public static class VoyageRowMapper implements RowMapper<Voyage> {
        public VoyageRowMapper() {
        }

        public Voyage mapRow(ResultSet rs, int rowNum) {
            try {
                return new Voyage(rs.getInt("PLAYER_ID"), rs.getInt("LEADER_ID"),
                        rs.getTimestamp("START_DATE"), rs.getTimestamp("FINISH_DATE"),
                        rs.getInt("FINISHED"), rs.getInt("TYPE"), rs.getInt("REWARD"));
            } catch (SQLException e) {
                return null;
            }
        }
    }
}
