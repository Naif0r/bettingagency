package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.dto.BetLegDTO;
import ro.ugal.aciee.bettingagency.dto.MatchTeamNamesDTO;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.model.enums.RateStatus;
import ro.ugal.aciee.bettingagency.model.enums.RateType;
import ro.ugal.aciee.bettingagency.utils.database.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {
    public Match save(Match match) throws SQLException {
        String sql = "INSERT INTO MATCH (sport_id, team1_id, team2_id, match_date)" +
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

    public Match importer(Match match) throws SQLException {
        String sql = """
                INSERT INTO MATCH (match_id, sport_id, team1_id, team2_id, match_date, match_status, team1_score, team2_score)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, match.getMatchId());
            stmt.setInt(2, match.getSportId());
            stmt.setInt(3, match.getTeam1Id());
            stmt.setInt(4, match.getTeam2Id());
            stmt.setTimestamp(5, Timestamp.valueOf(match.getMatchDate()));
            stmt.setString(6, match.getMatchStatus().name());
            stmt.setInt(7, match.getTeam1Score());
            stmt.setInt(8, match.getTeam2Score());
            stmt.executeUpdate();
            return match;
        }
    }

    public List<Match> getAll() throws SQLException {
        List<Match> matchList = new ArrayList<>();
        String sql = "SELECT * FROM MATCH ORDER BY match_date DESC";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matchList.add(mapMatch(rs));
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
                    return mapMatch(rs);
                }
            }
        }
        return null;
    }

    public List<Match> getBySport(String sportName) throws SQLException {
        List<Match> matchList = new ArrayList<>();
        String sql = "SELECT * FROM MATCH m JOIN SPORT s ON m.sport_id = s.sport_id WHERE s.sport_name = ? ORDER BY match_date DESC";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sportName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matchList.add(mapMatch(rs));
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
                    matchList.add(mapMatch(rs));
                }
            }
        }
        return matchList;
    }

    public List<Match> getByExistsStatus() throws SQLException {
        List<Match> matchList = new ArrayList<>();
        String sql = "SELECT * FROM MATCH WHERE match_status IN (?, ?) ORDER BY match_date ASC";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, MatchStatus.UPCOMING.name());
            stmt.setString(2, MatchStatus.LIVE.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matchList.add(mapMatch(rs));
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

    public List<BetLegDTO> getMatchInfoByBet(int betId) throws SQLException {
        List<BetLegDTO> betLegDTOLis = new ArrayList<>();
        String sql = """
                SELECT m.* , t1.team_name AS team1_name, t2.team_name AS team2_name, r.type AS rate_type FROM MATCH m
                JOIN TEAM t1 ON m.team1_id = t1.team_id
                JOIN TEAM t2 ON m.team2_id = t2.team_id
                JOIN RATE r ON m.match_id = r.match_id
                JOIN BET_RATE br ON r.rate_id = br.rate_id
                WHERE br.bet_id = ?;
                """;
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, betId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    betLegDTOLis.add(new BetLegDTO(mapMatch(rs),
                            rs.getString("team1_name"),
                            rs.getString("team2_name"),
                            RateType.valueOf(rs.getString("rate_type"))
                    ));
                }
            }
        }
        return betLegDTOLis;
    }

    public MatchTeamNamesDTO getMatchTeams(int matchId) throws SQLException {
        String sql = """
                SELECT t1.team_name AS team1_name, t2.team_name AS team2_name FROM MATCH m
                JOIN TEAM t1 ON m.team1_id = t1.team_id
                JOIN TEAM t2 ON m.team2_id = t2.team_id
                WHERE m.match_id = ?;
                """;
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new MatchTeamNamesDTO(rs.getString("team1_name"),
                            rs.getString("team2_name"));
                }
            }
        }
        return null;
    }

    private Match mapMatch(ResultSet rs) throws SQLException {
        return new Match(rs.getInt("match_id"),
                rs.getInt("sport_id"),
                rs.getInt("team1_id"),
                rs.getInt("team2_id"),
                rs.getTimestamp("match_date").toLocalDateTime(),
                MatchStatus.valueOf(rs.getString("match_status")),
                rs.getInt("team1_score"),
                rs.getInt("team2_score"));
    }

    private Rate mapRate(ResultSet rs) throws SQLException {
        return new Rate(rs.getInt("rate_id"),
                rs.getInt("match_id"),
                RateType.valueOf(rs.getString("type")),
                rs.getDouble("value"),
                RateStatus.valueOf(rs.getString("rate_status")));
    }
}
