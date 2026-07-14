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

    public Bet importer(Bet bet) throws SQLException {
        String sql = """
                INSERT INTO BET (bet_id, user_id, amount, bet_status, created_at, total_odds, possible_win)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, bet.getBetId());
            stmt.setInt(2, bet.getUserId());
            stmt.setDouble(3, bet.getAmount());
            stmt.setString(4, bet.getBetStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(bet.getCreatedAt()));
            stmt.setDouble(6, bet.getTotalOdds());
            stmt.setDouble(7, bet.getPossibleWin());
            stmt.executeUpdate();
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
        String sql = "SELECT * FROM BET WHERE user_id = ? ORDER BY created_at DESC";
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

    public List<Bet> getByStatus(BetStatus betStatus, int userId) throws SQLException {
        List<Bet> betList = new ArrayList<>();
        String sql = "SELECT * FROM BET WHERE bet_status = ? AND user_id = ? ORDER BY created_at DESC";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, betStatus.name());
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    betList.add(mapBet(rs));
                }
            }
        }
        return betList;
    }

    public List<Bet> getAllBetByMatch(int matchId) throws SQLException {
        List<Bet> betList = new ArrayList<>();
        String sql = """
                    SELECT b.* FROM BET b 
                    JOIN BET_RATE br ON b.bet_id = br.bet_id
                    JOIN RATE r ON br.rate_id = r.rate_id
                    JOIN MATCH m ON r.match_id = m.match_id
                    WHERE m.match_id = ?;
""";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
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
