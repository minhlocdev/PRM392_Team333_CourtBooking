package Models;

public class Court {
    private int courtId;
    private int courtOwnerId;
    private String courtName;
    private String openTime;
    private String closedTime;
    private String province;
    private String address;
    private String status;
    private byte[] image;

    public Court(int courtId, String status, String openTime, String closedTime, String address, String province, String courtName, int courtOwnerId, byte[] image) {
        this.courtId = courtId;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.status = status;
        this.address = address;
        this.province = province;
        this.courtName = courtName;
        this.courtOwnerId = courtOwnerId;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
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

    public String getProvince() {
        return province;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getClosedTime() {
        return closedTime;
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

    public void setProvince(String province) {
        this.province = province;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setClosedTime(String closedTime) {
        this.closedTime = closedTime;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

