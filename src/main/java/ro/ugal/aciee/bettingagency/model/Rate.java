package ro.ugal.aciee.bettingagency.model;

import ro.ugal.aciee.bettingagency.model.enums.RateStatus;
import ro.ugal.aciee.bettingagency.model.enums.RateType;

public class Rate {
    private final int rateId;
    private int matchId;
    private RateType type;
    private double value;
    private RateStatus rateStatus;

    public Rate(int rateId, int matchId, RateType type, double value, RateStatus rateStatus) {
        this.rateId = rateId;
        this.matchId = matchId;
        this.type = type;
        this.value = value;
        this.rateStatus = rateStatus;
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

    public RateStatus getRateStatus() {
        return rateStatus;
    }

    public void setRateStatus(RateStatus rateStatus) {
        this.rateStatus = rateStatus;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "rateId=" + rateId +
                ", matchId=" + matchId +
                ", type=" + type +
                ", value=" + value +
                ", rateStatus=" + rateStatus +
                '}';
    }
}
