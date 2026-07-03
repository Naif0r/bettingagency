package ro.ugal.aciee.bettingagency.model;

public class Sport {
    private final int sportId;
    private String sportName;

    public Sport(int sportId, String sportName) {
        this.sportId = sportId;
        this.sportName = sportName;
    }

    public int getSportId() {
        return sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    @Override
    public String toString() {
        return "Sport{" +
                "sportId=" + sportId +
                ", sportName='" + sportName + '\'' +
                '}';
    }
}
