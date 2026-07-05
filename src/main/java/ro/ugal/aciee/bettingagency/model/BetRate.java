package ro.ugal.aciee.bettingagency.model;

public class BetRate {
    private int betId;
    private int rateId;

    public BetRate() {
    }

    public BetRate(int betId, int rateId) {
        this.betId = betId;
        this.rateId = rateId;
    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    @Override
    public String toString() {
        return "BetRate{" +
                "betId=" + betId +
                ", rateId=" + rateId +
                '}';
    }
}
