package ro.ugal.aciee.bettingagency.utils.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseManager {
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

    private static final String SQL_DROP = """
                    DROP TABLE IF EXISTS BET_RATE CASCADE;
                    DROP TABLE IF EXISTS RATE CASCADE;
                    DROP TABLE IF EXISTS BET CASCADE;
                    DROP TABLE IF EXISTS MATCH CASCADE;
                    DROP TABLE IF EXISTS TEAM CASCADE;
                    DROP TABLE IF EXISTS SPORT CASCADE;
                    DROP TABLE IF EXISTS ACCOUNT CASCADE;
            """;

    private static final String SQL_DELETE = """
                      TRUNCATE TABLE
                                    BET_RATE,
                                    RATE,
                                    BET,
                                    MATCH,
                                    TEAM,
                                    SPORT,
                                    ACCOUNT
                                    RESTART IDENTITY CASCADE;
            """;

    private static final String SQL_SEQUENCE = """
            SELECT setval(pg_get_serial_sequence('account', 'user_id'),
                                COALESCE((SELECT MAX(user_id) FROM account), 1), true);
                    
            SELECT setval(pg_get_serial_sequence('sport', 'sport_id'),
                                COALESCE((SELECT MAX(sport_id) FROM sport), 1), true);
                    
            SELECT setval(pg_get_serial_sequence('team', 'team_id'),
                                COALESCE((SELECT MAX(team_id) FROM team), 1), true);
                    
            SELECT setval(pg_get_serial_sequence('match', 'match_id'),
                                COALESCE((SELECT MAX(match_id) FROM match), 1), true);
                    
            SELECT setval(pg_get_serial_sequence('rate', 'rate_id'),
                                COALESCE((SELECT MAX(rate_id) FROM rate), 1), true);
                    
            SELECT setval(pg_get_serial_sequence('bet', 'bet_id'),
                                COALESCE((SELECT MAX(bet_id) FROM bet), 1), true);
             """;

    public static boolean initialize() {
        try (Connection con = ConnectionManager.open();
             Statement stmt = con.createStatement()) {
            stmt.execute(SQL_CREATE_ACCOUNT);
            stmt.execute(SQL_CREATE_SPORT);
            stmt.execute(SQL_CREATE_TEAM);
            stmt.execute(SQL_CREATE_MATCH);
            stmt.execute(SQL_CREATE_RATE);
            stmt.execute(SQL_CREATE_BET);
            stmt.execute(SQL_CREATE_BET_RATE);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean drop() {
        try (Connection con = ConnectionManager.open();
             Statement stmt = con.createStatement()) {
            stmt.execute(SQL_DROP);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean delete() {
        try (Connection con = ConnectionManager.open();
             Statement stmt = con.createStatement()) {
            stmt.execute(SQL_DELETE);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean resetSequences(){
        try (Connection con = ConnectionManager.open();
             Statement stmt = con.createStatement()) {
            stmt.execute(SQL_SEQUENCE);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
