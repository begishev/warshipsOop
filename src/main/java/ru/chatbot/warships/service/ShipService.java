package ru.chatbot.warships.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Ship;
import ru.chatbot.warships.entity.ShipType;

import java.util.List;

public class ShipService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final static String GET_SHIP_BY_ID_SQL = "select s.ID, s.OWNER_ID," +
            "s.NAME, s.SPEED, s.POWER, s.TONNAGE, " +
            "st.NAME as TYPE_NAME, s.EMPLOYED, s.LOCATION " +
            "from ship s, ship_type st " +
            "where s.id = ? and s.type_id = st.id";
    private final static String GET_EMPLOYED_SHIP_SQL = "select s.ID, s.OWNER_ID," +
            "s.NAME, s.SPEED, s.POWER, s.TONNAGE, " +
            "st.NAME as TYPE_NAME, s.EMPLOYED, s.LOCATION " +
            "from ship s, ship_type st " +
            "where s.type_id = st.id " +
            "and s.owner_id = ? " +
            "and s.employed = 1";
    private final static String INSERT_SHIP_SQL = "insert into SHIP (OWNER_ID, NAME, TYPE_ID, SPEED, POWER, " +
            "TONNAGE, EMPLOYED, LOCATION) values(?, ?, ?, ?, ?, ?, 1, ?)";
    private final static String UNEMPLOY_SHIP_SQL = "update SHIP set EMPLOYED = 0 " +
            "where owner_id = ? " +
            "and employed = 1";
    private final static String GET_SHIP_TYPE_SQL = "select ID, NAME, MEAN_SPEED, SPEED_DEVIATION, " +
            "MEAN_POWER, POWER_DEVIATION, MEAN_TONNAGE, TONNAGE_DEVIATION, PRICE from SHIP_TYPE where ID = ?";
    private final static String GET_SHIP_TYPES_SQL = "select ID, NAME, MEAN_SPEED, SPEED_DEVIATION, " +
            "MEAN_POWER, POWER_DEVIATION, MEAN_TONNAGE, TONNAGE_DEVIATION, PRICE from SHIP_TYPE";

    private final static String UPDATE_LOCATION_SQL = "update SHIP set LOCATION = ? " +
            "where OWNER_ID = ? and EMPLOYED = 1";
    private final static String RENAME_SHIP_SQL = "update SHIP set NAME = ? " +
            "where OWNER_ID = ? and EMPLOYED = 1";

    public Ship getShip(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_SHIP_BY_ID_SQL, new Object[]{id}, new Ship.ShipRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * returns ship which is currently used by player
     **/
    public Ship getEmployedShip(Integer userId) {
        try {
            return jdbcTemplate.queryForObject(GET_EMPLOYED_SHIP_SQL, new Object[]{userId}, new Ship.ShipRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * Generate ship with random stats with given mean and deviation for this ship type
     * @param shipName name chosen by player
     * @param ownerId id of Player entity
     * @param typeId id of ShipType entity
     * @param locationId id of Port entity
     */
    public void createShip(String shipName, Integer ownerId, Long typeId, Integer locationId) {
        ShipType shipType = jdbcTemplate.queryForObject(GET_SHIP_TYPE_SQL,
                new Object[]{typeId}, new ShipType.ShipTypeRowMapper());

        Long speed =  shipType.getMeanSpeed() + (long)((Math.random() - 0.5) * 2 * shipType.getSpeedDeviation());
        Long power = shipType.getMeanPower() + (long)((Math.random() - 0.5) * 2 * shipType.getPowerDeviation());
        Long tonnage = shipType.getMeanTonnage() + (long)((Math.random() - 0.5) * 2 * shipType.getTonnageDeviation());
        jdbcTemplate.update(UNEMPLOY_SHIP_SQL, ownerId);
        jdbcTemplate.update(INSERT_SHIP_SQL, new Object[]{ownerId, shipName, typeId,
                speed, power, tonnage, locationId});
    }

    public void changeLocation(Player player, Port port) {
        jdbcTemplate.update(UPDATE_LOCATION_SQL, new Object[]{port.getId(), player.getId()});
    }

    public void renameShip(Integer playerId, String name) {
        jdbcTemplate.update(RENAME_SHIP_SQL, new Object[]{name, playerId});
    }

    public List<ShipType> getShipTypes() {
        try {
            return jdbcTemplate.query(GET_SHIP_TYPES_SQL, new ShipType.ShipTypeRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public ShipType getShipType(Long shipTypeId) {
        try {
            return jdbcTemplate.queryForObject(GET_SHIP_TYPE_SQL, new Object[]{shipTypeId}, new ShipType.ShipTypeRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

}
