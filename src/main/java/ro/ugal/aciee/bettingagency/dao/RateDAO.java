package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.enums.RateStatus;
import ro.ugal.aciee.bettingagency.model.enums.RateType;
import ro.ugal.aciee.bettingagency.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RateDAO {
    public boolean insert(Rate rate) throws SQLException {
        String sql = "INSERT INTO RATE (match_id, type, value, rate_status)" +
                     "VALUES (?, ?, ?, ?)";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, rate.getMatchId());
            stmt.setString(2, rate.getType().name());
            stmt.setDouble(3, rate.getValue());
            stmt.setString(4, rate.getRateStatus().name());
            return stmt.executeUpdate() == 1;
        }
    }

    public List<Rate> getAll() throws SQLException {
        List<Rate> rateList = new ArrayList<>();
        String sql = "SELECT * FROM RATE ";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    rateList.add(new Rate(rs.getInt("rate_id"),
                            rs.getInt("match_id"),
                            RateType.valueOf(rs.getString("type")),
                            rs.getDouble("value"),
                            RateStatus.valueOf(rs.getString("rate_status"))));
                }
            }
        }
        return rateList;
    }

    public List<Rate> getByMatch(int matchId) throws SQLException {
        List<Rate> rateList = new ArrayList<>();
        String sql = "SELECT * FROM RATE WHERE match_id = ?";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, matchId);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    rateList.add(new Rate(rs.getInt("rate_id"),
                            rs.getInt("match_id"),
                            RateType.valueOf(rs.getString("type")),
                            rs.getDouble("value"),
                            RateStatus.valueOf(rs.getString("rate_status"))));
                }
            }
        }
        return rateList;
    }

    public boolean updateStatus(int rateId, RateStatus rateStatus) throws SQLException{
        String sql = "UPDATE RATE SET rate_status = ? WHERE rate_id = ?";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1, rateStatus.name());
            stmt.setInt(2, rateId);
            return stmt.executeUpdate() == 1;
        }
    }
}
