package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.enums.BetStatus;
import ro.ugal.aciee.bettingagency.utils.database.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BetDAO {
    public Bet save(Bet bet) throws SQLException {
        String sql = "INSERT INTO BET (user_id, amount, total_odds, possible_win)" +
                "VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, bet.getUserId());
            stmt.setDouble(2, bet.getAmount());
            stmt.setDouble(3, bet.getTotalOdds());
            stmt.setDouble(4, bet.getPossibleWin());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                bet.setBetId(keys.getInt("bet_id"));
            }
            return bet;
        }
    }

    public List<Bet> getAll() throws SQLException {
        List<Bet> betList = new ArrayList<>();
        String sql = "SELECT * FROM BET";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    betList.add(mapBet(rs));
                }
            }
        }
        return betList;
    }

    public Bet getByBetId(int betId) throws SQLException {
        List<Bet> betList = new ArrayList<>();
        String sql = "SELECT * FROM BET WHERE bet_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, betId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapBet(rs);
                }
            }
        }
        return null;
    }

    public List<Bet> getByUserId(int userId) throws SQLException {
        List<Bet> betList = new ArrayList<>();
        String sql = "SELECT * FROM BET WHERE user_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    betList.add(mapBet(rs));
                }
            }
        }
        return betList;
    }

    public List<Bet> getByStatus(BetStatus betStatus) throws SQLException {
        List<Bet> betList = new ArrayList<>();
        String sql = "SELECT * FROM BET WHERE bet_status = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, betStatus.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    betList.add(mapBet(rs));
                }
            }
        }
        return betList;
    }

    public boolean updateStatus(BetStatus betStatus, int betId) throws SQLException {
        String sql = "UPDATE BET SET bet_status = ? WHERE bet_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, betStatus.name());
            stmt.setInt(2, betId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int betId) throws SQLException {
        String sql = "DELETE FROM BET WHERE bet_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, betId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Bet mapBet(ResultSet rs) throws SQLException {
        return new Bet(rs.getInt("bet_id"),
                rs.getInt("user_id"),
                rs.getDouble("amount"),
                BetStatus.valueOf(rs.getString("bet_status")),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getDouble("total_odds"),
                rs.getDouble("possible_win"));
    }
}
