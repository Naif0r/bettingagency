package ro.ugal.aciee.bettingagency.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String SQL_CREATE_ACCOUNT = """
            CREATE TABLE IF NOT EXISTS ACCOUNT (
                                     user_id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     role VARCHAR(20) NOT NULL,
                                     account_status VARCHAR(20) DEFAULT 'ACTIVE',
                                     balance DECIMAL(10, 2) DEFAULT 0.00
            );
            """;
    private static final String SQL_CREATE_SPORT = """
            CREATE TABLE IF NOT EXISTS SPORT (
                                    sport_id SERIAL PRIMARY KEY,
                                    sport_name VARCHAR(50) UNIQUE NOT NULL
            );
            """;
    private static final String SQL_CREATE_TEAM = """
            CREATE TABLE IF NOT EXISTS TEAM (
                                    team_id SERIAL PRIMARY KEY,
                                    sport_id INT REFERENCES Sport(sport_id) ON DELETE CASCADE,
                                    team_name VARCHAR(100) NOT NULL,
                                    UNIQUE (sport_id, team_name)
            );
            """;
    private static final String SQL_CREATE_MATCH = """
            CREATE TABLE IF NOT EXISTS MATCH (
                                    match_id SERIAL PRIMARY KEY,
                                    sport_id INT REFERENCES Sport(sport_id),
                                    team1_id INT REFERENCES Team(team_id),
                                    team2_id INT REFERENCES Team(team_id),
                                    match_date TIMESTAMP NOT NULL,
                                    match_status VARCHAR(20) DEFAULT 'UPCOMING',
            		                team1_score INT DEFAULT 0,
            		                team2_score INT DEFAULT 0
            );
            """;
    private static final String SQL_CREATE_RATE = """
            CREATE TABLE IF NOT EXISTS RATE (
                                    rate_id SERIAL PRIMARY KEY,
                                    match_id INT REFERENCES Match(match_id) ON DELETE CASCADE,
                                    type VARCHAR(20) NOT NULL,
                                    value DECIMAL(5, 2) NOT NULL,
                                    rate_status VARCHAR(20) DEFAULT 'OPEN',
                                    UNIQUE (match_id, type)
            );
            """;
    private static final String SQL_CREATE_BET = """
            CREATE TABLE IF NOT EXISTS BET (
                                    bet_id SERIAL PRIMARY KEY,
                                    user_id INT REFERENCES ACCOUNT(user_id) ON DELETE CASCADE,
                                    amount DECIMAL(10, 2) NOT NULL,
                                    bet_status VARCHAR(20) DEFAULT 'PENDING',
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    total_odds DECIMAL(5, 2),
                                    possible_win DECIMAL(10, 2)
            );
            """;
    private static final String SQL_CREATE_BET_RATE = """
            CREATE TABLE IF NOT EXISTS BET_RATE (
                                    bet_id INT REFERENCES Bet(bet_id) ON DELETE CASCADE,
                                    rate_id INT REFERENCES Rate(rate_id) ON DELETE CASCADE,
                                    PRIMARY KEY (bet_id, rate_id)
            );
            """;

    public static void initialize() throws SQLException {
        try (Connection con = ConnectionManager.open();
             Statement stmt = con.createStatement()) {
            stmt.execute(SQL_CREATE_ACCOUNT);
            stmt.execute(SQL_CREATE_SPORT);
            stmt.execute(SQL_CREATE_TEAM);
            stmt.execute(SQL_CREATE_MATCH);
            stmt.execute(SQL_CREATE_RATE);
            stmt.execute(SQL_CREATE_BET);
            stmt.execute(SQL_CREATE_BET_RATE);
        }
    }
}
