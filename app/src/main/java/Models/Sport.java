package Models;

public class Sport {
    private int sportId;
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

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
}

