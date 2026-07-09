package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.utils.database.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {
    public Team save(Team team) throws SQLException {
        String sql = "INSERT INTO TEAM (sport_id, team_name)" +
                "VALUES (?, ?)";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, team.getSportId());
            stmt.setString(2, team.getTeamName());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                team.setTeamId(keys.getInt("team_id"));
            }
            return team;
        }
    }

    public List<Team> getAll() throws SQLException {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT * FROM TEAM";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teamList.add(mapTeam(rs));
                }
            }
        }
        return teamList;
    }

    public List<Team> getBySport(String sportName) throws SQLException {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT * FROM TEAM t JOIN SPORT s ON t.sport_id = s.sport_id WHERE s.sport_name = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sportName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teamList.add(mapTeam(rs));
                }
            }
        }
        return teamList;
    }

    public Team getById(int teamId) throws SQLException {
        String sql = "SELECT * FROM TEAM WHERE team_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapTeam(rs);
                }
            }
        }
        return null;
    }

    public boolean updateTeamName(String teamName, int teamId) throws SQLException {
        String sql = "UPDATE TEAM SET team_name = ? WHERE team_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, teamName);
            stmt.setInt(2, teamId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int teamId) throws SQLException {
        String sql = "DELETE FROM TEAM WHERE team_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Team mapTeam(ResultSet rs) throws SQLException {
        return new Team(rs.getInt("team_id"),
                rs.getInt("sport_id"),
                rs.getString("team_name"));
    }

}
