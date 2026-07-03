package ro.ugal.aciee.bettingagency.model;

import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;

import java.time.LocalDateTime;

public class Match {
    private final int matchId;
    private int sportId;
    private int team1Id;
    private int team2Id;
    private LocalDateTime matchDate;
    private MatchStatus matchStatus;

    public Match(int matchId, int sportId, int team1Id, int team2Id, LocalDateTime matchDate, MatchStatus matchStatus) {
        this.matchId = matchId;
        this.sportId = sportId;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.matchDate = matchDate;
        this.matchStatus = matchStatus;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(int team1Id) {
        this.team1Id = team1Id;
    }

    public int getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(int team2Id) {
        this.team2Id = team2Id;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", sportId=" + sportId +
                ", team1Id=" + team1Id +
                ", team2Id=" + team2Id +
                ", matchDate=" + matchDate +
                ", matchStatus=" + matchStatus +
                '}';
    }
}
