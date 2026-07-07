package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.MatchDAO;
import ro.ugal.aciee.bettingagency.dao.SportDAO;
import ro.ugal.aciee.bettingagency.dao.TeamDAO;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Sport;
import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MatchService {
    private final MatchDAO matchDAO = new MatchDAO();
    private final SportDAO sportDAO = new SportDAO();
    private final TeamDAO teamDAO = new TeamDAO();

    public Match save(int sportId, int team1Id, int team2Id, LocalDateTime matchDate) throws SQLException {
        if (sportId <= 0 || team1Id <= 0 || team2Id <= 0) {
            throw new IllegalArgumentException("Incorrect id");
        }
        Sport sport = sportDAO.getById(sportId);
        Team team1 = teamDAO.getById(team1Id);
        Team team2 = teamDAO.getById(team2Id);
        if (sport == null) {
            throw new IllegalArgumentException("Sport not found");
        }
        if (team1 == null) {
            throw new IllegalArgumentException("Team1 not found");
        }
        if (team2 == null) {
            throw new IllegalArgumentException("Team2 not found");
        }
        if (matchDate == null) {
            throw new IllegalArgumentException("Time cannot be empty");
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

    public List<Match> getAll() throws SQLException {
        List<Match> matchList = matchDAO.getAll();
        if (matchList.isEmpty()) {
            throw new IllegalArgumentException("Match list is empty");
        }
        return matchList;
    }

    public Match getById(int matchId) throws SQLException {
        if (matchId <= 0){
            throw new IllegalArgumentException("Incorrect match id");
        }
        Match match = matchDAO.getById(matchId);
        if(match == null){
            throw new IllegalArgumentException("Match not found");
        }
        return match;
    }

    public List<Match> getBySport(String sportName) throws SQLException {
        if (sportName.isBlank()) {
            throw new IllegalArgumentException("Sport name is empty");
        }
        List<Match> matchList = matchDAO.getBySport(sportName);
        if (matchList.isEmpty()) {
            throw new IllegalArgumentException("Match list is empty");
        }
        return matchList;
    }

    public List<Match> getByStatus(MatchStatus matchStatus) throws SQLException {
        if (matchStatus == null) {
            throw new IllegalArgumentException("Incorrect match status");
        }
        List<Match> matchList = matchDAO.getByStatus(matchStatus);
        if (matchList.isEmpty()) {
            throw new IllegalArgumentException("Match list is empty");
        }
        return matchList;
    }

    public boolean updateStatus(MatchStatus matchStatus, int matchId) throws SQLException {
        if (matchId <= 0) {
            throw new IllegalArgumentException("Incorrect match id");
        }
        if (matchStatus == null) {
            throw new IllegalArgumentException("Incorrect match status");
        }
        Match match = matchDAO.getById(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match not found");
        }
        if (match.getMatchStatus().equals(matchStatus)) {
            throw new IllegalArgumentException("It is not possible to change the status because it is already set");
        }
        return matchDAO.updateStatus(matchStatus, matchId);
    }

    public boolean delete(int matchId) throws SQLException {
        if (matchId <= 0) {
            throw new IllegalArgumentException("Incorrect match id");
        }
        if (matchDAO.getById(matchId) == null) {
            throw new IllegalArgumentException("Match not found");
        }
        return matchDAO.delete(matchId);
    }

}
