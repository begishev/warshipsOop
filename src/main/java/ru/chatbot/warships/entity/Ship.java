package ru.chatbot.warships.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ship {
    private Long id;
    private Integer ownerId;
    private String name;
    private String typeName;
    private Long speed;
    private Long power;
    private Long tonnage;
    private Boolean employed;
    private Integer locationId;

    public Ship(Long id, Integer ownerId, String name, String typeName, Long speed, Long power,
                Long tonnage, Boolean employed, Integer locationId) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.typeName = typeName;
        this.speed = speed;
        this.power = power;
        this.tonnage = tonnage;
        this.employed = employed;
        this.locationId = locationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }

    public Long getPower() {
        return power;
    }

    public void setPower(Long power) {
        this.power = power;
    }

    public Long getTonnage() {
        return tonnage;
    }

    public void setTonnage(Long tonnage) {
        this.tonnage = tonnage;
    }

    public Boolean getEmployed() {
        return employed;
    }

    public void setEmployed(Boolean employed) {
        this.employed = employed;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public static class ShipRowMapper implements RowMapper<Ship> {
        public ShipRowMapper() {
        }

        public Ship mapRow(ResultSet rs, int rowNum) {
            try {
                return new Ship(rs.getLong("ID"), rs.getInt("OWNER_ID"), rs.getString("NAME"),
                        rs.getString("TYPE_NAME"), rs.getLong("SPEED"), rs.getLong("POWER"),
                        rs.getLong("TONNAGE"), rs.getBoolean("EMPLOYED"), rs.getInt("LOCATION"));
            } catch (SQLException e) {
                return null;
            }
        }
    }
}
