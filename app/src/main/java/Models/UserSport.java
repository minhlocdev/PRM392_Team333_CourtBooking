package Models;

public class UserSport {
    private int sportId;
    private int userId;
    private String level;

    public UserSport(int sportId, int userId, String level) {
        this.sportId = sportId;
        this.userId = userId;
        this.level = level;
    }

    public int getSportId() {
        return sportId;
    }

    public int getUserId() {
        return userId;
    }

    public String getLevel() {
        return level;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

