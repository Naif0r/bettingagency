package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {
    public Match save(Match match) throws SQLException {
        String sql = "INSERT INTO MATCH (sport_id, team1_id, team2_id, match_date, team1_score, team2_score)" +
                "VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, match.getSportId());
            stmt.setInt(2, match.getTeam1Id());
            stmt.setInt(3, match.getTeam2Id());
            stmt.setTimestamp(4, Timestamp.valueOf(match.getMatchDate()));
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                match.setMatchId(keys.getInt("match_id"));
            }
            return match;
        }
    }

    public List<Match> getAll() throws SQLException {
        List<Match> matchList = new ArrayList<>();
        String sql = "SELECT * FROM MATCH";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matchList.add(new Match(rs.getInt("match_id"),
                            rs.getInt("sport_id"),
                            rs.getInt("team1_id"),
                            rs.getInt("team2_id"),
                            rs.getTimestamp("match_date").toLocalDateTime(),
                            MatchStatus.valueOf(rs.getString("match_status")),
                            rs.getInt("team1_score"),
                            rs.getInt("team2_score")));
                }
            }
        }
        return matchList;
    }

    public Match getById(int matchId) throws SQLException {
        String sql = "SELECT * FROM MATCH WHERE match_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Match(rs.getInt("match_id"),
                            rs.getInt("sport_id"),
                            rs.getInt("team1_id"),
                            rs.getInt("team2_id"),
                            rs.getTimestamp("match_date").toLocalDateTime(),
                            MatchStatus.valueOf(rs.getString("match_status")),
                            rs.getInt("team1_score"),
                            rs.getInt("team2_score"));
                }
            }
        }
        return null;
    }

    public List<Match> getBySport(String sportName) throws SQLException {
        List<Match> matchList = new ArrayList<>();
        String sql = "SELECT * FROM MATCH m JOIN SPORT s ON m.sport_id = s.sport_id WHERE s.sport_name = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sportName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matchList.add(new Match(rs.getInt("match_id"),
                            rs.getInt("sport_id"),
                            rs.getInt("team1_id"),
                            rs.getInt("team2_id"),
                            rs.getTimestamp("match_date").toLocalDateTime(),
                            MatchStatus.valueOf(rs.getString("match_status")),
                            rs.getInt("team1_score"),
                            rs.getInt("team2_score")));
                }
            }
        }
        return matchList;
    }

    public List<Match> getByStatus(MatchStatus matchStatus) throws SQLException {
        List<Match> matchList = new ArrayList<>();
        String sql = "SELECT * FROM MATCH WHERE match_status = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, matchStatus.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matchList.add(new Match(rs.getInt("match_id"),
                            rs.getInt("sport_id"),
                            rs.getInt("team1_id"),
                            rs.getInt("team2_id"),
                            rs.getTimestamp("match_date").toLocalDateTime(),
                            MatchStatus.valueOf(rs.getString("match_status")),
                            rs.getInt("team1_score"),
                            rs.getInt("team2_score")));
                }
            }
        }
        return matchList;
    }

    public boolean updateStatus(MatchStatus matchStatus, int id) throws SQLException {
        String sql = "UPDATE MATCH SET match_status = ? WHERE match_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, matchStatus.name());
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int matchId) throws SQLException {
        String sql = "DELETE FROM MATCH WHERE match_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateScoreTeam1(int matchId) throws SQLException {
        String sql = "UPDATE MATCH SET team1_score = team1_score + 1 WHERE match_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateScoreTeam2(int matchId) throws SQLException {
        String sql = "UPDATE MATCH SET team2_score = team2_score + 1 WHERE match_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            return stmt.executeUpdate() > 0;
        }
    }

}
