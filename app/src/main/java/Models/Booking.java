package Models;

import java.io.Serializable;

public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    private int bookingId;
    private int courtId;
    private int playerId;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private double price;
    private String reason;
    private String status;
    private String createdAt;


    public Booking(int bookingId, int courtId, int playerId, String bookingDate, String startTime, String endTime, double price, String status, String createdAt, String reason) {
        this.bookingId = bookingId;
        this.courtId = courtId;
        this.playerId = playerId;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
        this.reason = reason;
    }

    public String getReason() {return reason;}

    public void setReason(String reason) {this.reason = reason;}

    public int getBookingId() {
        return bookingId;
    }

    public int getCourtId() {
        return courtId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
