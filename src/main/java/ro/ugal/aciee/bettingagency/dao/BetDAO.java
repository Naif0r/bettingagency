package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.enums.BetStatus;
import ro.ugal.aciee.bettingagency.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BetDAO {
    public boolean insert(Bet bet) throws SQLException {
        String sql = "INSERT INTO BET (user_id, amount, bet_status, created_at, total_odds, possible_win)" +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, bet.getUserId());
            stmt.setDouble(2, bet.getAmount());
            stmt.setString(3, bet.getBetStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(bet.getCreatedAt()));
            stmt.setDouble(5, bet.getTotalOdds());
            stmt.setDouble(6, bet.getPossibleWin());
            return stmt.executeUpdate() == 1;
        }
    }

    public List<Bet> getByUserId(int userId) throws SQLException {
        List<Bet> betList = new ArrayList<>();
        String sql = "SELECT * FROM BET WHERE user_id = ?";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, userId);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    betList.add(new Bet(rs.getInt("bet_id"),
                            rs.getInt("user_id"),
                            rs.getDouble("amount"),
                            BetStatus.valueOf(rs.getString("bet_status")),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getDouble("total_odds"),
                            rs.getDouble("possible_win")));
                }
            }
        }
        return betList;
    }
}
