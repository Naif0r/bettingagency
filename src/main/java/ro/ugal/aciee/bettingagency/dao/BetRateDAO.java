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
    public boolean insert(BetRate betRate) throws SQLException {
        String sql = "INSERT INTO BET_RATE (bet_id, rate_id)" +
                     "VALUES (?, ?)";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, betRate.getBetId());
            stmt.setInt(2, betRate.getRateId());
            return stmt.executeUpdate() == 1;
        }
    }

    public List<BetRate> getByBetId(int betId) throws SQLException {
        List<BetRate> betRateList = new ArrayList<>();
        String sql = "SELECT * FROM BET_RATE WHERE bet_id = ?";
        try (Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, betId);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    betRateList.add(new BetRate(rs.getInt("bet_id"),
                            rs.getInt("rate_id")));
                }
            }
        }
        return betRateList;
    }
}
