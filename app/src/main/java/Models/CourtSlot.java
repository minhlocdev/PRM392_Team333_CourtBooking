package Models;

public class CourtSlot {
    private int courtSlotId;
    private int courtId;
    private String timeStart;
    private String timeEnd;
    private double cost;

    public CourtSlot(int courtSlotId, int courtId, String timeStart, String timeEnd, double cost) {
        this.courtSlotId = courtSlotId;
        this.courtId = courtId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.cost = cost;
    }

    public int getCourtSlotId() {
        return courtSlotId;
    }

    public int getCourtId() {
        return courtId;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public void setCourtSlotId(int courtSlotId) {
        this.courtSlotId = courtSlotId;
    }
}
