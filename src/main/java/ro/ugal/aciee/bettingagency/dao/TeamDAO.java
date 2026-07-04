package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {
    public boolean insert(Team team) throws SQLException {
        String sql = "INSERT INTO TEAM (sport_id, team_name)" +
                "VALUES (?, ?)";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, team.getSportId());
            stmt.setString(2, team.getTeamName());
            return stmt.executeUpdate() == 1;
        }
    }

    public List<Team> getAll() throws SQLException {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT * FROM TEAM";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teamList.add(new Team(rs.getInt("team_id"),
                            rs.getInt("sport_id"),
                            rs.getString("team_name")));
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
                    teamList.add(new Team(rs.getInt("team_id"),
                            rs.getInt("sport_id"),
                            rs.getString("team_name")));
                }
            }
        }
        return teamList;
    }

}
