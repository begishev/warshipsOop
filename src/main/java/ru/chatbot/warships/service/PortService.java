package ru.chatbot.warships.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Team;

import java.util.List;

public class PortService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_PORT_SQL = "SELECT ID, NAME, X, Y, OWNER, POWER FROM PORT WHERE ID = ?";
    private static final String GET_ENEMNY_PORTS_SQL = "SELECT p.ID, p.NAME, p.X, p.Y, p.OWNER, d.DISTANCE, d.REWARD " +
            "FROM PORT p, ROUTE d " +
            "WHERE " +
            "(" +
            "(d.FROM_PORT = ? " +
            "AND d.TO_PORT = p.ID)" +
            "OR " +
            "(d.TO_PORT = ? " +
            "AND d.FROM_PORT = p.ID)" +
            ") " +
            "AND p.OWNER != ?";
    private static final String GET_ALLY_PORTS_SQL = "SELECT p.ID, p.NAME, p.X, p.Y, p.OWNER, d.DISTANCE, d.REWARD " +
            "FROM PORT p, ROUTE d " +
            "WHERE " +
            "(" +
            "(d.FROM_PORT = ? " +
            "AND d.TO_PORT = p.ID)" +
            "OR " +
            "(d.TO_PORT = ? " +
            "AND d.FROM_PORT = p.ID)" +
            ") " +
            "AND p.OWNER = ?";

    private static final String CHANGE_PORT_OWNER_SQL = "UPDATE PORT SET OWNER = ?, POWER = ? WHERE ID = ?";

    private static final Integer DEFAULT_LOCATION_BRITAIN = 2;
    private static final Integer DEFAULT_LOCATION_SPAIN = 3;

    public Port getPort(Integer id) {
        try {
            return jdbcTemplate.queryForObject(GET_PORT_SQL, new Object[]{id}, new Port.PortRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * Get all ports which belong to the enemy fraction and can be accessed from specified location
     *
     * This method may be used to get list of attack directions from current user's location
     * @param portId current player's location
     * @param team player's team
     * @return list of accessible enemy ports
     */
    public List<Port> getEnemyPorts(Integer portId, Team team) {
        try {
            return jdbcTemplate.query(GET_ENEMNY_PORTS_SQL, new Object[]{portId, portId, team.getTeamId()}, new Port.PortWithDistanceRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * Get all ports which belong to the same fraction and can be accessed from specified location
     *
     * This method may be used to get list of trade routes from current user's location
     * @param portId current player's location
     * @param team player's team
     * @return list of accessible ally ports
     */
    public List<Port> getAllyPorts(Integer portId, Team team) {
        try {
            return jdbcTemplate.query(GET_ALLY_PORTS_SQL, new Object[]{portId, portId, team.getTeamId()}, new Port.PortWithDistanceRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * Update information about port based on passed argument
     *
     * Change port owner and decrease power of fortification
     * @param port object storing new owner and new power
     */
    public void capturePort(Port port) {
        jdbcTemplate.update(CHANGE_PORT_OWNER_SQL, port.getOwner().getTeamId(), port.getPower(), port.getId());

    }

    public static Integer getDefaultLocationBritain() {
        return DEFAULT_LOCATION_BRITAIN;
    }

    public static Integer getDefaultLocationSpain() {
        return DEFAULT_LOCATION_SPAIN;
    }
}
