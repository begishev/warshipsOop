package ru.chatbot.warships.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Team;

public class PlayerService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_PLAYER_BY_ID_SQL = "select ID, CHAT_ID, NICKNAME, TEAM, GOLD from PLAYER where ID = ?";
    private static final String INSERT_PLAYER_SQL = "insert into PLAYER (ID, CHAT_ID, NICKNAME, TEAM, GOLD) values(?, ?, ?, ?, 0)";
    private static final String CHANGE_NICKNAME_SQL = "update PLAYER set NICKNAME = ? where ID = ?";

    public Player getPlayer(Integer id) {
        try {
            return jdbcTemplate.queryForObject(GET_PLAYER_BY_ID_SQL, new Object[]{id}, new Player.PlayerRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void createPlayer(Integer userId, Long chatId, String nickname, Team team) throws DataAccessException {
        jdbcTemplate.update(INSERT_PLAYER_SQL, userId, chatId, nickname, team.getTeamId());
    }

    public void setNickname(Integer playerId, String nickname) {
        jdbcTemplate.update(CHANGE_NICKNAME_SQL, nickname, playerId);
    }
}
