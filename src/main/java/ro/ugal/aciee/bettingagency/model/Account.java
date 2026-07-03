package ro.ugal.aciee.bettingagency.model;

import ro.ugal.aciee.bettingagency.model.enums.AccountStatus;
import ro.ugal.aciee.bettingagency.model.enums.Role;

public class Account {
    private final int userId;
    private String userName;
    private String password;
    private Role role;
    private AccountStatus accountStatus;
    private double balance;

    public Account(int userId, String userName, String password, Role role, AccountStatus accountAccountStatus, double balance) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.accountStatus = accountAccountStatus;
        this.balance = balance;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", accountStatus=" + accountStatus +
                ", balance=" + balance +
                '}';
    }
}


