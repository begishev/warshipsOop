package ru.chatbot.warships.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShipType {
    private Long id;
    private String name;
    private Long meanSpeed;
    private Long speedDeviation;
    private Long meanPower;
    private Long powerDeviation;
    private Long meanTonnage;
    private Long tonnageDeviation;

    public ShipType(Long id, String name, Long meanSpeed, Long speedDeviation, Long meanPower,
                    Long powerDeviation, Long meanTonnage, Long tonnageDeviation) {
        this.id = id;
        this.name = name;
        this.meanSpeed = meanSpeed;
        this.speedDeviation = speedDeviation;
        this.meanPower = meanPower;
        this.powerDeviation = powerDeviation;
        this.meanTonnage = meanTonnage;
        this.tonnageDeviation = tonnageDeviation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMeanSpeed() {
        return meanSpeed;
    }

    public void setMeanSpeed(Long meanSpeed) {
        this.meanSpeed = meanSpeed;
    }

    public Long getSpeedDeviation() {
        return speedDeviation;
    }

    public void setSpeedDeviation(Long speedDeviation) {
        this.speedDeviation = speedDeviation;
    }

    public Long getMeanPower() {
        return meanPower;
    }

    public void setMeanPower(Long meanPower) {
        this.meanPower = meanPower;
    }

    public Long getPowerDeviation() {
        return powerDeviation;
    }

    public void setPowerDeviation(Long powerDeviation) {
        this.powerDeviation = powerDeviation;
    }

    public Long getMeanTonnage() {
        return meanTonnage;
    }

    public void setMeanTonnage(Long meanTonnage) {
        this.meanTonnage = meanTonnage;
    }

    public Long getTonnageDeviation() {
        return tonnageDeviation;
    }

    public void setTonnageDeviation(Long tonnageDeviation) {
        this.tonnageDeviation = tonnageDeviation;
    }

    public static class ShipTypeRowMapper implements RowMapper<ShipType> {
        public ShipTypeRowMapper() {
        }

        public ShipType mapRow(ResultSet rs, int rowNum) {
            try {
                return new ShipType(rs.getLong("ID"), rs.getString("NAME"),
                        rs.getLong("MEAN_SPEED"), rs.getLong("SPEED_DEVIATION"),
                        rs.getLong("MEAN_POWER"), rs.getLong("POWER_DEVIATION"),
                        rs.getLong("MEAN_TONNAGE"), rs.getLong("TONNAGE_DEVIATION"));
            } catch (SQLException e) {
                return null;
            }
        }
    }
}
