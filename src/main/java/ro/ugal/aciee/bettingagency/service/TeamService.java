package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.TeamDAO;
import ro.ugal.aciee.bettingagency.model.Sport;
import ro.ugal.aciee.bettingagency.model.Team;

import java.sql.SQLException;
import java.util.List;

public class TeamService {
    private final TeamDAO teamDAO = new TeamDAO();
    private final SportService sportService = new SportService();

    public Team save(int sportId, String teamName) throws SQLException {
        Sport sport = sportService.getById(sportId);

        teamName = teamName.trim();

        if (teamName.isBlank()) {
            throw new IllegalArgumentException("Team name is empty");
        }

        List<Team> teamList = teamDAO.getBySport(sport.getSportName());

        for (Team teamTest : teamList) {
            if (teamTest.getTeamName().equals(teamName)) {
                throw new IllegalArgumentException("There cannot be two identical teams for one sport");
            }
        }

        Team team = new Team();
        team.setSportId(sportId);
        team.setTeamName(teamName);

        return teamDAO.save(team);
    }

    public Team importer(Team team) {
        try {
            return teamDAO.importer(team);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<Team> getAll() throws SQLException {
        return teamDAO.getAll();
    }

    public List<Team> getBySport(String sportName) throws SQLException {
        sportName = sportName.trim();

        if (sportName.isBlank()) {
            throw new IllegalArgumentException("Sport name is blank");
        }

        return teamDAO.getBySport(sportName);
    }

    public Team getById(int teamId) throws SQLException {
        if (teamId <= 0) {
            throw new IllegalArgumentException("Incorrect team id");
        }

        Team team = teamDAO.getById(teamId);

        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        return team;
    }

    public Team getByTeamAndSport(String teamName, String sportName) throws SQLException {
        teamName = teamName.trim();
        sportName = sportName.trim();

        if (teamName.isBlank() || sportName.isBlank()) {
            throw new IllegalArgumentException("Name(team/sport) is blank");
        }

        Team team = teamDAO.getByTeamNameAndSport(teamName, sportName);

        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        return team;
    }

    public boolean updateTeamName(String newTeamName, int teamId) throws SQLException {
        newTeamName = newTeamName.trim();
        Team team = getById(teamId);

        if (newTeamName.isBlank()) {
            throw new IllegalArgumentException("Team name is blank");
        }

        if (team.getTeamName().equals(newTeamName)) {
            throw new IllegalArgumentException("The new team name should not repeat the old one");
        }

        List<Team> teamList = teamDAO.getBySport(sportService.getById(team.getSportId()).getSportName());

        for (Team teamTest : teamList) {
            if (teamTest.getTeamName().equals(newTeamName)) {
                throw new IllegalArgumentException("There cannot be two identical teams for one sport");
            }
        }

        return teamDAO.updateTeamName(newTeamName, teamId);
    }

    public boolean delete(int teamId) throws SQLException {
        getById(teamId);
        return teamDAO.delete(teamId);
    }
}
