package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.dto.MatchOddDTO;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.enums.RateStatus;
import ro.ugal.aciee.bettingagency.model.enums.RateType;
import ro.ugal.aciee.bettingagency.utils.database.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RateDAO {
    public Rate save(Rate rate) throws SQLException {
        String sql = "INSERT INTO RATE (match_id, type, value)" +
                "VALUES (?, ?, ?)";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rate.getMatchId());
            stmt.setString(2, rate.getType().name());
            stmt.setDouble(3, rate.getValue());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                rate.setRateId(keys.getInt("rate_id"));
            }
            return rate;
        }
    }

    public Rate importer(Rate rate) throws SQLException {
        String sql = """
                INSERT INTO RATE (rate_id, match_id, type, value, rate_status)
                VALUES (?, ?, ?, ?, ?);
                """;
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, rate.getRateId());
            stmt.setInt(2, rate.getMatchId());
            stmt.setString(3, rate.getType().name());
            stmt.setDouble(4, rate.getValue());
            stmt.setString(5, rate.getRateStatus().name());
            stmt.executeUpdate();
            return rate;
        }
    }

    public List<Rate> getAll() throws SQLException {
        List<Rate> rateList = new ArrayList<>();
        String sql = "SELECT * FROM RATE ";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rateList.add(mapRate(rs));
                }
            }
        }
        return rateList;
    }

    public List<Rate> getByMatch(int matchId) throws SQLException {
        List<Rate> rateList = new ArrayList<>();
        String sql = "SELECT * FROM RATE WHERE match_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rateList.add(mapRate(rs));
                }
            }
        }
        return rateList;
    }

    public Rate getById(int rateId) throws SQLException {
        String sql = "SELECT * FROM RATE WHERE rate_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, rateId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRate(rs);
                }
            }
        }
        return null;
    }

    public List<MatchOddDTO> getRatesWithTeamNames(int matchId) throws SQLException {
        List<MatchOddDTO> matchOddDTOList = new ArrayList<>();
        String sql = """
                SELECT r.*, t1.team_name AS team1_name, t2.team_name AS team2_name FROM RATE r 
                JOIN MATCH m ON r.match_id = m.match_id
                JOIN TEAM t1 ON m.team1_id = t1.team_id
                JOIN TEAM t2 ON m.team2_id = t2.team_id
                WHERE m.match_id = ?;
                """;
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matchOddDTOList.add(new MatchOddDTO(rs.getString("team1_name"),
                            rs.getString("team2_name"),
                            mapRate(rs)));
                }
            }
        }
        return matchOddDTOList;
    }

    public boolean updateStatus(int rateId, RateStatus rateStatus) throws SQLException {
        String sql = "UPDATE RATE SET rate_status = ? WHERE rate_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, rateStatus.name());
            stmt.setInt(2, rateId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int rateId) throws SQLException {
        String sql = "DELETE FROM RATE WHERE rate_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, rateId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Rate mapRate(ResultSet rs) throws SQLException {
        return new Rate(rs.getInt("rate_id"),
                rs.getInt("match_id"),
                RateType.valueOf(rs.getString("type")),
                rs.getDouble("value"),
                RateStatus.valueOf(rs.getString("rate_status")));
    }
}
