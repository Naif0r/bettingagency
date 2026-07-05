package ro.ugal.aciee.bettingagency.model;

import ro.ugal.aciee.bettingagency.model.enums.AccountStatus;
import ro.ugal.aciee.bettingagency.model.enums.Role;

public class Account {
    private int userId;
    private String username;
    private String password;
    private Role role;
    private AccountStatus accountStatus;
    private double balance;

    public Account() {
    }

    public Account(int userId, String username, String password, Role role, AccountStatus accountAccountStatus, double balance) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.accountStatus = accountAccountStatus;
        this.balance = balance;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountAccountStatus) {
        this.accountStatus = accountAccountStatus;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId=" + userId +
                ", userName='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", accountStatus=" + accountStatus +
                ", balance=" + balance +
                '}';
    }
}


