package Models;

public class BookingSlot {
    private int bookingId;

    private int courtSlotId;

    private String status;

    public BookingSlot(int bookingId, int courtSlotId, String status) {
        this.bookingId = bookingId;
        this.courtSlotId = courtSlotId;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getCourtSlotId() {
        return courtSlotId;
    }

    public String getStatus() {
        return status;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setCourtSlotId(int courtSlotId) {
        this.courtSlotId = courtSlotId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
