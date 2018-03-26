package ru.chatbot.warships.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.chatbot.warships.entity.*;

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

    private final static String GET_TRAVEL_BY_PLAYER_ID_SQL = "select PLAYER_ID, START_DATE, FINISH_DATE, DESTINATION, STATUS " +
            "from TRAVEL " +
            "where PLAYER_ID = ? and STATUS = 0";

    private final static String GET_TRADE_BY_ID_SQL = "select PLAYER_ID, LEADER_ID, START_DATE, FINISH_DATE, REWARD, DESTINATION, STATUS " +
            "from TRADE " +
            "where PLAYER_ID = ? and STATUS = 0";

    public static Long calculateRouteTime(Long distance, Ship ship) {
        return distance / ship.getSpeed();
    }

    public Voyage getVoyage(Player player) {
        try {
            return jdbcTemplate.queryForObject(GET_TRAVEL_BY_PLAYER_ID_SQL,
                    new Object[]{player.getId()}, new Travel.TravelRowMapper());
        } catch (EmptyResultDataAccessException e) {
            try {
                return jdbcTemplate.queryForObject(GET_TRADE_BY_ID_SQL,
                        new Object[]{player.getId()}, new Trade.TradeRowMapper());
            } catch (DataAccessException ex) {
                return null;
            }
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

    public List<Travel> startHandlingArrivedTravelers() {
        try {
            jdbcTemplate.update(START_ARRIVED_TRAVELERS_HANDLING_SQL);
            return jdbcTemplate.queryForList(GET_ARRIVED_TRAVELERS_SQL).stream()
                    .map(rs -> new Travel((Integer) rs.get("PLAYER_ID"), (Integer) rs.get("DESTINATION"),
                            (Timestamp) rs.get("START_DATE"), (Timestamp) rs.get("FINISH_DATE"), 1))
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

    public List<Trade> startHandlingArrivedTraders() {
        try {
            jdbcTemplate.update(START_ARRIVED_TRADERS_HANDLING_SQL);
            return jdbcTemplate.queryForList(GET_ARRIVED_TRADERS_SQL).stream()
                    .map(rs -> new Trade((Integer) rs.get("PLAYER_ID"), (Integer) rs.get("LEADER_ID"),
                            (Integer) rs.get("DESTINATION"), (Timestamp) rs.get("START_DATE"),
                            (Timestamp) rs.get("FINISH_DATE"), (Integer) rs.get("REWARD"), 1))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void finishHandlingArrivedTraders() {
        jdbcTemplate.update(FINISH_ARRIVED_TRADERS_HANDLING_SQL);
    }

}
