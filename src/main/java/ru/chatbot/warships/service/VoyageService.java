package ru.chatbot.warships.service;

import org.glassfish.grizzly.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Route;
import ru.chatbot.warships.entity.Ship;
import ru.chatbot.warships.entity.Voyage;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class VoyageService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShipService shipService;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static String GET_DISTANCE_SQL = "select FROM_PORT, TO_PORT, DISTANCE, REWARD from ROUTE " +
            "where ((TO_PORT = ?) and (FROM_PORT = ?)) " +
            "or " +
            "((FROM_PORT = ?) and (TO_PORT = ?))";

    private final static String GET_VOYAGE_BY_ID_SQL = "select PLAYER_ID, LEADER_ID, START_DATE, FINISH_DATE, " +
            "FINISHED, TYPE, REWARD from VOYAGE where PLAYER_ ID = ?";

    private final static String INSERT_VOYAGE_SQL = "insert into VOYAGE ( PLAYER_ID, LEADER_ID, START_DATE, FINISH_DATE," +
            "FINISHED, TYPE, REWARD) values(?, ?, CURRENT_TIMESTAMP , DATE_ADD(CURRENT_TIMESTAMP, ? MINUTE), 0, ?, ?)";

    private final static String GET_PLAYERS_WITH_SAME_LEADER_SQL = "select PLAYER_ID from VOYAGE where (? = LEADER_ID)";

    private final static String UPDATE_ARRIVED_PLAYERS_SQL = "update VOYAGE set FINISHED = 1 where (CURRENT_TIMESTAMP > FINISH_DATE)";

    private final static String GET_ARRIVED_PLAYERS_SQL = "select PLAYER_ID from VOYAGE where (FINISHED = 1)";

    private final static String PREPARE_TO_DELETE_ARRIVED_PLAYERS_SQL = "update VOYAGE set FINISHED = 2 " +
            "where (FINISHED = 1)";

    private final static String START_ARRIVED_TRAVELERS_HANDLING_SQL = "update TRAVEL set STATUS = 1 " +
            "where FINISH_DATE < now() and STATUS = 0";

    private final static String START_ARRIVED_TRADERS_HANDLING_SQL = "update TRADE set STATUS = 1 " +
            "where FINISH_DATE < now() and STATUS = 0";

    private final static String GET_ARRIVED_TRAVELERS_SQL = "select PLAYER_ID, START_DATE, FINISH_DATE, DESTINATION from TRAVEL " +
            "where STATUS = 1";

    private final static String GET_ARRIVED_TRADERS_SQL = "select PLAYER_ID, START_DATE, FINISH_DATE, DESTINATION " +
            "from TRADE " +
            "where STATUS = 1";

    private final static String FINISH_ARRIVED_TRAVELERS_HANDLING_SQL = "update TRAVEL set STATUS = 2 " +
            "where STATUS = 1";

    private final static String FINISH_ARRIVED_TRADERS_HANDLING_SQL = "update TRADE set STATUS = 2 " +
            "where STATUS = 1";

    private final static String CREATE_TRAVEL_SQL = "insert into TRAVEL " +
            "(PLAYER_ID, START_DATE, FINISH_DATE, DESTINATION, STATUS) " +
            "values(?, now(), DATEADD('MINUTE', ?, NOW()), ?, 0)";

    private final static String CREATE_TRADE_SQL = "insert into TRADE " +
            "(PLAYER_ID, LEADER_ID, START_DATE, FINISH_DATE, REWARD, DESTINATION, STATUS) " +
            "values(?, ?, now(), DATEADD('MINUTE', ?, NOW()), ?, ?, 0)";

    public static Long calculateRouteTime(Long distance, Ship ship) {
        return distance / ship.getSpeed();
    }

    public Voyage getVoyage(Player player) {
        try {
            return jdbcTemplate.queryForObject(GET_VOYAGE_BY_ID_SQL, new Object[]{player.getId()}, new Voyage.VoyageRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void createVoyage(List<Pair<Player, Integer>> playersWithRewards, Player leader, Integer type, Long duration) {
        for (Pair<Player, Integer> player : playersWithRewards) {
            jdbcTemplate.update(INSERT_VOYAGE_SQL, player.getFirst().getId(),
                    leader.getId(), duration, type, player.getSecond());
        }
    }

    public List<Integer> getArrivedPlayers() {
        try {
            jdbcTemplate.update(UPDATE_ARRIVED_PLAYERS_SQL);
            List<Integer> result = jdbcTemplate.queryForList(GET_ARRIVED_PLAYERS_SQL, Integer.class);
            jdbcTemplate.update(PREPARE_TO_DELETE_ARRIVED_PLAYERS_SQL);
            return result;
        } catch (DataAccessException e) {
            return null;
        }
    }

    public List<Integer> getPlayersWithSameLeader(Player player) {
        try {
            return jdbcTemplate.queryForList(GET_PLAYERS_WITH_SAME_LEADER_SQL, new Object[]{player.getId()}, Integer.class);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void createTravel(Player player, Integer from, Integer to) {
        try {
            Ship ship = shipService.getEmployedShip(player.getId());
            Route route = jdbcTemplate.queryForObject(GET_DISTANCE_SQL, new Object[]{from, to, from, to}, new Route.RouteRowMapper());
            Long routeTime = calculateRouteTime(route.getDistance(), ship);
            jdbcTemplate.update(CREATE_TRAVEL_SQL, player.getId(), routeTime, to);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public List<Voyage> startHandlingArrivedTravelers() {
        try {
            jdbcTemplate.update(START_ARRIVED_TRAVELERS_HANDLING_SQL);
            return jdbcTemplate.queryForList(GET_ARRIVED_TRAVELERS_SQL).stream()
                    .map(rs -> new Voyage((Integer) rs.get("PLAYER_ID"), (Integer) rs.get("DESTINATION"),
                            (Timestamp) rs.get("START_DATE"), (Timestamp) rs.get("FINISH_DATE")))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void finishHandlingArrivedTravelers() {
        jdbcTemplate.update(FINISH_ARRIVED_TRAVELERS_HANDLING_SQL);
    }

    public void createTrade(Player player, Player leader, Integer from, Integer to) {
        try {
            Ship ship = shipService.getEmployedShip(player.getId());
            Route route = jdbcTemplate.queryForObject(GET_DISTANCE_SQL, new Object[]{from, to, from, to}, new Route.RouteRowMapper());
            Long routeTime = calculateRouteTime(route.getDistance(), ship);
            jdbcTemplate.update(CREATE_TRADE_SQL, player.getId(), leader.getId(), routeTime, route.getReward(), to);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public List<Voyage> startHandlingArrivedTraders() {
        try {
            jdbcTemplate.update(START_ARRIVED_TRADERS_HANDLING_SQL);
            return jdbcTemplate.queryForList(GET_ARRIVED_TRADERS_SQL).stream()
                    .map(rs -> new Voyage((Integer) rs.get("PLAYER_ID"), (Integer) rs.get("LEADER_ID"),
                            (Integer) rs.get("DESTINATION"), (Timestamp) rs.get("START_DATE"),
                            (Timestamp) rs.get("FINISH_DATE"), (Integer) rs.get("REWARD")))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void finishHandlingArrivedTraders() {
        jdbcTemplate.update(FINISH_ARRIVED_TRADERS_HANDLING_SQL);
    }

}
