package ru.chatbot.warships.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Trade extends Voyage {
    private Integer playerId;
    private Integer leaderId;
    private Integer destination;
    private Timestamp startTime;
    private Timestamp finishTime;
    private Integer finished;
    private Integer reward;

    public Trade(Integer playerId, Integer leaderId, Integer destination, Timestamp startTime, Timestamp finishTime, Integer finished, Integer reward) {
        this.playerId = playerId;
        this.leaderId = leaderId;
        this.destination = destination;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.finished = finished;
        this.reward = reward;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public static class TradeRowMapper implements RowMapper<Trade> {
        public TradeRowMapper() {
        }

        public Trade mapRow(ResultSet rs, int rowNum) {
            try {
                return new Trade(rs.getInt("PLAYER_ID"), rs.getInt("LEADER_ID"),
                        rs.getInt("DESTINATION"), rs.getTimestamp("START_DATE"),
                        rs.getTimestamp("FINISH_DATE"), rs.getInt("STATUS"),
                        rs.getInt("REWARD"));
            } catch (SQLException e) {
                return null;
            }
        }
    }
}
