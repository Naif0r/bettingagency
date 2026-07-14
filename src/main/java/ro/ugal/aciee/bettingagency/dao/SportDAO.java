package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Sport;
import ro.ugal.aciee.bettingagency.utils.database.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SportDAO {
    public Sport save(Sport sport) throws SQLException {
        String sql = "INSERT INTO SPORT(sport_name)" +
                "VALUES (?)";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sport.getSportName());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                sport.setSportId(keys.getInt("sport_id"));
            }
            return sport;
        }
    }

    public Sport importer(Sport sport) throws SQLException {
        String sql = """
                INSERT INTO SPORT (sport_id, sport_name)
                VALUES (?, ?);
                """;
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, sport.getSportId());
            stmt.setString(2, sport.getSportName());
            stmt.executeUpdate();
            return sport;
        }
    }

    public List<Sport> getAll() throws SQLException {
        List<Sport> sportList = new ArrayList<>();
        String sql = "SELECT * FROM SPORT";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sportList.add(mapSport(rs));
                }
            }
        }
        return sportList;
    }

    public Sport getById(int sportId) throws SQLException {
        String sql = "SELECT * FROM SPORT WHERE sport_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sportId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapSport(rs);
                }
            }
        }
        return null;
    }

    public Sport getBySportName(String sportName) throws SQLException {
        String sql = "SELECT * FROM SPORT WHERE sport_name = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sportName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapSport(rs);
                }
            }
        }
        return null;
    }

    public boolean update(String sportName, int sportId) throws SQLException {
        String sql = "UPDATE SPORT SET sport_name = ? WHERE sport_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sportName);
            stmt.setInt(2, sportId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int sportId) throws SQLException {
        String sql = "DELETE FROM SPORT WHERE sport_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sportId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Sport mapSport(ResultSet rs) throws SQLException {
        return new Sport(rs.getInt("sport_id"),
                rs.getString("sport_name"));
    }
}
