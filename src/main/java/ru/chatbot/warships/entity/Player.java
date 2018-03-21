package ru.chatbot.warships.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class Player {
    Integer id;
    Long chatId;
    String nickname;
    Team team;
    Long gold;

    public Player(Integer id, Long chatId, String nickname, Team team, Long gold) {
        this.id = id;
        this.chatId = chatId;
        this.nickname = nickname;
        this.team = team;
        this.gold = gold;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Long getGold() {
        return this.gold;
    }

    public void setGold(Long gold) {
        this.gold = gold;
    }

    public static class PlayerRowMapper implements RowMapper<Player> {
        public PlayerRowMapper() {
        }

        public Player mapRow(ResultSet rs, int rowNum) {
            try {
                return new Player(rs.getInt("ID"), rs.getLong("CHAT_ID"),
                        rs.getString("NICKNAME"), Team.valueOf(rs.getInt("TEAM")),
                        rs.getLong("GOLD"));
            } catch (SQLException e) {
                return null;
            }
        }
    }
}
