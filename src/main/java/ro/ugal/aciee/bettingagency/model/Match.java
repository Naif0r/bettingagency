package ro.ugal.aciee.bettingagency.model;

import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;

import java.time.LocalDateTime;

public class Match {
    private int matchId;
    private int sportId;
    private int team1Id;
    private int team2Id;
    private LocalDateTime matchDate;
    private MatchStatus matchStatus;
    private int team1Score;
    private int team2Score;

    public Match() {
    }

    public Match(int matchId, int sportId, int team1Id, int team2Id, LocalDateTime matchDate, MatchStatus matchStatus, int team1Score, int team2Score) {
        this.matchId = matchId;
        this.sportId = sportId;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.matchDate = matchDate;
        this.matchStatus = matchStatus;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
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

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
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
                ", team1Score=" + team1Score +
                ", team2Score=" + team2Score +
                '}';
    }
}
