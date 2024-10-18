package Models;

public class User {
    private int userId;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private byte[] avatar;
    private String dateCreated;
    private boolean isActive;

    public User(int userId, String password, String fullName, String email, String phone, byte[] avatar, String dateCreated, boolean isActive) {
        this.userId = userId;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateCreated = dateCreated;
        this.isActive = isActive;
    }

    public byte[] getAvatar() {return avatar;}

    public void setAvatar(byte[] avatar) {this.avatar = avatar;}

    public int getUserId() {
        return userId;
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

    public void setUserId(int userId) {
        this.userId = userId;
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
}

