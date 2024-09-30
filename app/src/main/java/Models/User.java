package Models;

public class User {
    private int userId;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String dateCreated;
    private boolean isActive;

    public User(int userId, String userName, String fullName, String email, String phone, String role, String dateCreated, boolean isActive) {
        this.userId = userId;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.dateCreated = dateCreated;
        this.isActive = isActive;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
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

    public String getRole() {
        return role;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setRole(String role) {
        this.role = role;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

