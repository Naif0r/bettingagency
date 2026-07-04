package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Sport;
import ro.ugal.aciee.bettingagency.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SportDAO {
    public boolean insert(Sport sport) throws SQLException {
        String sql = "INSERT INTO SPORT(sport_name)" +
                     "VALUES (?)";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1, sport.getSportName());
            return stmt.executeUpdate() == 1;
        }
    }

    public List<Sport> getAll() throws SQLException{
        List<Sport> sportList = new ArrayList<>();
        String sql = "SELECT * FROM SPORT";
        try(Connection con = ConnectionManager.open();
            PreparedStatement stmt = con.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    sportList.add( new Sport(rs.getInt("sport_id"),
                            rs.getString("sport_name")));
                }
            }
        }
        return sportList;
    }
}
