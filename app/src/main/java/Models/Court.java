package Models;

public class Court {
    private int courtId;
    private int courtOwnerId;
    private String courtName;
    private String location;
    private String address;
    private String status;
    private int sportId;

    public Court(int courtId, int sportId, String status, String address, String location, String courtName, int courtOwnerId) {
        this.courtId = courtId;
        this.sportId = sportId;
        this.status = status;
        this.address = address;
        this.location = location;
        this.courtName = courtName;
        this.courtOwnerId = courtOwnerId;
    }

    public int getCourtId() {
        return courtId;
    }

    public int getCourtOwnerId() {
        return courtOwnerId;
    }

    public String getCourtName() {
        return courtName;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public int getSportId() {
        return sportId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public void setCourtOwnerId(int courtOwnerId) {
        this.courtOwnerId = courtOwnerId;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }
}

