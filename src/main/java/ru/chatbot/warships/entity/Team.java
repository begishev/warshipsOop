package ru.chatbot.warships.entity;

public enum Team {
    BRITAIN(1),
    SPAIN(2);

    Integer teamId;

    private Team(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getTeamId() {
        return this.teamId;
    }

    public static Team valueOf(Integer teamId) {
        Team team = null;
        switch(teamId) {
            case 1:
                team = BRITAIN;
                break;
            case 2:
                team = SPAIN;
        }

        return team;
    }
}