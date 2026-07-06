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
        existsId(id);
        return accountDAO.getById(id);
    }

    public Account getByUsername(String username) throws SQLException {
        if (username.isBlank()) {
            throw new IllegalArgumentException("Username is empty");
        }
        Account account = accountDAO.getByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    public Account register(String username, String password, Role role) throws SQLException {
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
        existsId(id);
        if (accountStatus == null) {
            throw new IllegalArgumentException("Status is empty");
        }
        if (accountStatus.equals(accountDAO.getById(id).getAccountStatus())) {
            throw new IllegalArgumentException("It is not possible to change the status because it is already set");
        }
        return accountDAO.updateStatus(accountStatus, id);
    }

    public boolean updateUsername(String username, int id) throws SQLException {
        existsId(id);
        if (accountDAO.getByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        return accountDAO.updateUsername(username, id);
    }

    public boolean updatePassword(String password, int id) throws SQLException {
        existsId(id);
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password is too short");
        }
        if (accountDAO.getById(id).getPassword().equals(password)) {
            throw new IllegalArgumentException("New password must be different from the current password");
        }
        return accountDAO.updatePassword(password, id);
    }

    public boolean updateBalance(int id, double balance) throws SQLException {
        existsId(id);
        if (balance <= 0) {
            throw new IllegalArgumentException("Balance must be minim 1");
        }
        return accountDAO.updateBalance(balance, id);
    }

    public boolean delete(int id) throws SQLException {
        existsId(id);
        return accountDAO.delete(id);
    }

    private void existsId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }
        Account account = accountDAO.getById(id);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
    }
}
