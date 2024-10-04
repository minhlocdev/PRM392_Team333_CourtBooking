package Models;

public class CourtOwner {
    private int courtOwnerId;
    private String fullName;
    private String password;
    private String email;
    private String taxCode;
    private String phone;
    private String dateCreated;
    private boolean isActive;

    public CourtOwner(int courtOwnerId, String password, String fullName, String email, String phone,  String dateCreated, boolean isActive, String taxCode) {
        this.courtOwnerId = courtOwnerId;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateCreated = dateCreated;
        this.isActive = isActive;
        this.taxCode = taxCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public int getCourtOwnerId() {
        return courtOwnerId;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }


    public String getDateCreated() {
        return dateCreated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setCourtOwnerId(int courtOwnerId) {
        this.courtOwnerId = courtOwnerId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
}
