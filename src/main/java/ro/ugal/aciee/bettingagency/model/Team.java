package ro.ugal.aciee.bettingagency.model;

public class Team {
    private int teamId;
    private int sportId;
    private String teamName;

    public Team() {
    }

    public Team(int teamId, int sportId, String teamName) {
        this.teamId = teamId;
        this.sportId = sportId;
        this.teamName = teamName;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", sportId=" + sportId +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}
