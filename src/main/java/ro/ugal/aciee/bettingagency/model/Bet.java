package ro.ugal.aciee.bettingagency.model;

import ro.ugal.aciee.bettingagency.model.enums.BetStatus;

import java.time.LocalDateTime;

public class Bet {
    private final int betId;
    private int userId;
    private double amount;
    private BetStatus betStatus;
    private LocalDateTime createdAt;

    public Bet(int betId, int userId, double amount, BetStatus betStatus, LocalDateTime createdAt) {
        this.betId = betId;
        this.userId = userId;
        this.amount = amount;
        this.betStatus = betStatus;
        this.createdAt = createdAt;
    }

    public int getBetId() {
        return betId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BetStatus getBetStatus() {
        return betStatus;
    }

    public void setBetStatus(BetStatus betStatus) {
        this.betStatus = betStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "betId=" + betId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", betStatus=" + betStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
