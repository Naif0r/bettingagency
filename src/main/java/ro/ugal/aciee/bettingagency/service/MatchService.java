package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.MatchDAO;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.model.enums.MatchTeam;
import ro.ugal.aciee.bettingagency.model.enums.RateType;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MatchService {
    private final MatchDAO matchDAO = new MatchDAO();
    private final SportService sportService = new SportService();
    private final TeamService teamService = new TeamService();

    public Match save(int sportId, int team1Id, int team2Id, LocalDateTime matchDate) throws SQLException {
        sportService.getById(sportId);

        Team team1 = teamService.getById(team1Id);
        Team team2 = teamService.getById(team2Id);

        if (matchDate == null) {
            throw new IllegalArgumentException("Time cannot be empty");
        }

        if (matchDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Match date cannot be in the past");
        }

        if (team1.getSportId() != sportId) {
            throw new IllegalArgumentException("Team1 must be from this sport");
        }

        if (team2.getSportId() != sportId) {
            throw new IllegalArgumentException("Team2 must be from this sport");
        }

        if (team1.getTeamId() == team2.getTeamId()) {
            throw new IllegalArgumentException("Team can not play against itself");
        }

        Match match = new Match();
        match.setSportId(sportId);
        match.setTeam1Id(team1Id);
        match.setTeam2Id(team2Id);
        match.setMatchDate(matchDate);

        return matchDAO.save(match);
    }

    public Match importer(Match match) {
        try {
            return matchDAO.importer(match);
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<Match> getAll() throws SQLException {
        return matchDAO.getAll();
    }

    public Match getById(int matchId) throws SQLException {
        if (matchId <= 0) {
            throw new IllegalArgumentException("Incorrect match id");
        }

        Match match = matchDAO.getById(matchId);

        if (match == null) {
            throw new IllegalArgumentException("Match not found");
        }

        return match;
    }

    public List<Match> getBySport(String sportName) throws SQLException {
        sportName = sportName.trim();

        if (sportName.isBlank()) {
            throw new IllegalArgumentException("Sport name is empty");
        }

        return  matchDAO.getBySport(sportName);
    }

    public List<Match> getByStatus(MatchStatus matchStatus) throws SQLException {
        if (matchStatus == null) {
            throw new IllegalArgumentException("Incorrect match status");
        }

        return matchDAO.getByStatus(matchStatus);
    }

    public List<Match> getByExistsStatus() throws SQLException {
        return matchDAO.getByExistsStatus();
    }

    public boolean updateStatus(MatchStatus matchStatus, int matchId) throws SQLException {
        Match match = getById(matchId);

        if (matchStatus == null) {
            throw new IllegalArgumentException("Incorrect match status");
        }

        if (match.getMatchStatus() == matchStatus) {
            throw new IllegalArgumentException("It is not possible to change the status because it is already set");
        }

        if (match.getMatchStatus() == MatchStatus.FINISHED) {
            throw new IllegalArgumentException("The match has already ended, and its status cannot be changed");
        }

        if (match.getMatchStatus() == MatchStatus.CANCELLED) {
            throw new IllegalArgumentException("The match has been canceled, and its status cannot be changed");
        }

        if (match.getMatchStatus() == MatchStatus.LIVE) {
            if (matchStatus == MatchStatus.UPCOMING) {
                throw new IllegalArgumentException("You cannot change the status from LIVE to UPCOMING");
            }
        }

        return matchDAO.updateStatus(matchStatus, matchId);
    }

    public boolean updateScoreTeam(int matchId, MatchTeam matchTeam) throws SQLException {
        Match match = getById(matchId);

        if(match.getMatchStatus() != MatchStatus.LIVE){
            throw new IllegalArgumentException("Cannot be used for this match");
        }

        if (matchTeam == null) {
            throw new IllegalArgumentException("Incorrect match team");
        }

        if (matchTeam == MatchTeam.TEAM1) {
            return matchDAO.updateScoreTeam1(matchId);
        } else if (matchTeam == MatchTeam.TEAM2) {
            return matchDAO.updateScoreTeam2(matchId);
        }

        throw new IllegalArgumentException("Unknown team");
    }

    public RateType getWinnerMatch(int matchId) throws SQLException {
        Match match = getById(matchId);

        int team1Score = match.getTeam1Score();
        int team2Score = match.getTeam2Score();

        if (match.getMatchStatus() != MatchStatus.FINISHED) {
            throw new IllegalArgumentException("The match must be finished");
        }

        if (team1Score > team2Score) {
            return RateType.WIN1;
        }

        if (team1Score == team2Score) {
            return RateType.DRAW;
        }

        return RateType.WIN2;
    }

    public boolean delete(int matchId) throws SQLException {
        getById(matchId);
        return matchDAO.delete(matchId);
    }

}
