package ru.chatbot.warships.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Ship;
import ru.chatbot.warships.entity.Team;

public class PlayerService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private ShipService shipService;

    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }

    @Autowired
    private PortService portService;

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    private static final String GET_PLAYER_BY_ID_SQL = "select ID, CHAT_ID, NICKNAME, TEAM, GOLD from PLAYER where ID = ?";
    private static final String INSERT_PLAYER_SQL = "insert into PLAYER (ID, CHAT_ID, NICKNAME, TEAM, GOLD) values(?, ?, ?, ?, 0)";
    private static final String CHECK_ARRIVAL_SQL = "select * from " +
            "(select TEAM from PLAYER where ID = ?) a, " +
            "(select OWNER from PORT where ID = ?) b";
    private static final String INCREASE_GOLD_SQL = "update PLAYER set GOLD = GOLD + ? " +
            "where ID = ?";
    private static final String CHANGE_NICKNAME_SQL = "update PLAYER set NICKNAME = ? where ID = ?";

    private static final Long DEFAULT_SHIP_ID = 1L;

    public Player getPlayer(Integer id) {
        try {
            return jdbcTemplate.queryForObject(GET_PLAYER_BY_ID_SQL, new Object[]{id}, new Player.PlayerRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void createPlayer(Integer userId, Long chatId, String nickname, Team team) throws DataAccessException {
        jdbcTemplate.update(INSERT_PLAYER_SQL, new Object[]{userId, chatId, nickname, team.getTeamId()});
        shipService.createShip("My first ship", userId, DEFAULT_SHIP_ID, getPlayerLocation(userId));
    }

    /**
     * Player location is location of currently employed ship
     * If player has no ship then it returns default port for every team
     */
    public Integer getPlayerLocation(Integer id) {
        Player player = getPlayer(id);
        if (player != null) {
            Ship ship = shipService.getEmployedShip(id);
            if (ship != null) {
                return ship.getLocationId();
            } else {
                switch (player.getTeam()) {
                    case BRITAIN:
                        return PortService.getDefaultLocationBritain();
                    case SPAIN:
                        return PortService.getDefaultLocationSpain();
                    default:
                        return null;
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Change location of user
     *
     * It's possible for enemy to capture port while one on his way to it
     * So it helps to check if player can arrive to port
     * @return true if ship successfully arrives and false otherwise
     */
    public boolean arrive(Player player, Port destination) {
        destination = portService.getPort(destination.getId());
        if (!destination.getOwner().equals(player.getTeam())) {
            return false;
        } else {
            shipService.changeLocation(player, destination);
            return true;
        }
    }

    public void giveGold(Player player, Long gold) {
        jdbcTemplate.update(INCREASE_GOLD_SQL, gold, player.getId());
    }

    public void setNickname(Integer playerId, String nickname) {
        jdbcTemplate.update(CHANGE_NICKNAME_SQL, nickname, playerId);
    }
}
