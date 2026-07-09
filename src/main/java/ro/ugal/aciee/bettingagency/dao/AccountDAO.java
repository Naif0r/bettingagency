package ro.ugal.aciee.bettingagency.dao;

import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.enums.AccountStatus;
import ro.ugal.aciee.bettingagency.model.enums.Role;
import ro.ugal.aciee.bettingagency.utils.database.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public List<Account> getAll() throws SQLException {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNT";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accountList.add(mapAccount(rs));
                }
            }
        }
        return accountList;
    }

    public Account getById(int id) throws SQLException {
        String sql = "SELECT * FROM account WHERE user_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            }
        }
        return null;
    }

    public Account getByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM ACCOUNT WHERE username = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            }
        }
        return null;
    }

    public boolean updateUsername(String username, int id) throws SQLException {
        String sql = "UPDATE ACCOUNT SET username = ? WHERE user_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updatePassword(String password, int id) throws SQLException {
        String sql = "UPDATE ACCOUNT SET password = ? WHERE user_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(AccountStatus accountStatus, int id) throws SQLException {
        String sql = "UPDATE ACCOUNT SET account_status = ? WHERE user_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, accountStatus.name());
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateBalance(double balance, int id) throws SQLException {
        String sql = "UPDATE ACCOUNT SET balance = balance + ? WHERE user_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, balance);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public Account save(Account account) throws SQLException {
        String sql = "INSERT INTO ACCOUNT (username, password, role) " +
                "VALUES (?, ?, ?)";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getRole().name());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                account.setUserId(keys.getInt("user_id"));
            }
            return account;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM ACCOUNT WHERE user_id = ?";
        try (Connection con = ConnectionManager.open();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Account mapAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role")),
                AccountStatus.valueOf(rs.getString("account_status")),
                rs.getDouble("balance"));
    }
}
