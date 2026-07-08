package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.AccountDAO;
import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.enums.AccountStatus;
import ro.ugal.aciee.bettingagency.model.enums.Role;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();

    public List<Account> getAll() throws SQLException {
        List<Account> accountList = accountDAO.getAll();
        if (accountList.isEmpty()) {
            throw new IllegalArgumentException("Account list is empty");
        }
        return accountList;
    }

    public Account getById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }
        Account account = accountDAO.getById(id);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    public Account getByUsername(String username) throws SQLException {
        username = username.trim();
        if (username.isBlank()) {
            throw new IllegalArgumentException("Username is blank");
        }
        Account account = accountDAO.getByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    public Account register(String username, String password, Role role) throws SQLException {
        username = username.trim();
        password = password.trim();
        if (username.isBlank()) {
            throw new IllegalArgumentException("Username is empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password is too short");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role is empty");
        }
        if (accountDAO.getByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole(role);
        return accountDAO.save(account);
    }

    public boolean updateStatus(int id, AccountStatus accountStatus) throws SQLException {
        Account account = getById(id);
        if (accountStatus == null) {
            throw new IllegalArgumentException("Status is empty");
        }
        if (accountStatus == account.getAccountStatus()) {
            throw new IllegalArgumentException("It is not possible to change the status because it is already set");
        }
        return accountDAO.updateStatus(accountStatus, id);
    }

    public boolean updateUsername(String newUsername, int id) throws SQLException {
        Account account = getById(id);
        newUsername = newUsername.trim();
        if (newUsername.isBlank()) {
            throw new IllegalArgumentException("New username is blank");
        }
        if (account.getUsername().equals(newUsername)) {
            throw new IllegalArgumentException("New username must be different from the current username");
        }
        if (accountDAO.getByUsername(newUsername) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        return accountDAO.updateUsername(newUsername, id);
    }

    public boolean updatePassword(String newPassword, int id) throws SQLException {
        Account account = getById(id);
        newPassword = newPassword.trim();
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password is too short");
        }
        if (account.getPassword().equals(newPassword)) {
            throw new IllegalArgumentException("New password must be different from the current password");
        }
        return accountDAO.updatePassword(newPassword, id);
    }

    public boolean updateBalance(int id, double balance) throws SQLException {
        getById(id);
        return accountDAO.updateBalance(balance, id);
    }

    public boolean deposit(int userId, double amount) throws SQLException {
        getById(userId);
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
        return accountDAO.updateBalance(amount, userId);
    }

    public boolean delete(int id) throws SQLException {
        getById(id);
        return accountDAO.delete(id);
    }

    public Account login(String username, String password) throws SQLException {
        username = username.trim();
        password = password.trim();
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password is blank");
        }
        Account account = getByUsername(username);
        if (!account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password");
        }
        return account;
    }
}
