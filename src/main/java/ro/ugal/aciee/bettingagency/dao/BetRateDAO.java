package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.BetRate;
import ro.ugal.aciee.bettingagency.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BetRateDAO {
    public BetRate save(BetRate betRate) throws SQLException {
        String sql = "INSERT INTO BET_RATE (bet_id, rate_id)" +
                "VALUES (?, ?)";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, betRate.getBetId());
            stmt.setInt(2, betRate.getRateId());
            stmt.executeUpdate();
            return betRate;
        }
    }

    public List<BetRate> getByBetId(int betId) throws SQLException {
        List<BetRate> betRateList = new ArrayList<>();
        String sql = "SELECT * FROM BET_RATE WHERE bet_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, betId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    betRateList.add(new BetRate(rs.getInt("bet_id"),
                            rs.getInt("rate_id")));
                }
            }
        }
        return betRateList;
    }

    public List<BetRate> getByRateId(int rateId) throws SQLException {
        List<BetRate> betRateList = new ArrayList<>();
        String sql = "SELECT * FROM BET_RATE WHERE rate_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, rateId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    betRateList.add(new BetRate(rs.getInt("bet_id"),
                            rs.getInt("rate_id")));
                }
            }
        }
        return betRateList;
    }

    public boolean delete(int betId, int rateId) throws SQLException {
        String sql = "DELETE FROM BET_RATE WHERE bet_id = ? AND rate_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, betId);
            stmt.setInt(2, rateId);
            return stmt.executeUpdate() > 0;
        }
    }
}
