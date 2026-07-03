package ro.ugal.aciee.bettingagency.model;

import ro.ugal.aciee.bettingagency.model.enums.RateType;

public class Rate {
    private final int rateId;
    private int matchId;
    private RateType type;
    private double value;

    public Rate(int rateId, int matchId, RateType type, double value) {
        this.rateId = rateId;
        this.matchId = matchId;
        this.type = type;
        this.value = value;
    }

    public int getRateId() {
        return rateId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public RateType getType() {
        return type;
    }

    public void setType(RateType type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "rateId=" + rateId +
                ", matchId=" + matchId +
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
