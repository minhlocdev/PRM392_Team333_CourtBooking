package Models;

import androidx.annotation.NonNull;

public class CourtListDown {
    private int courtId;
    private String courtName;

    public CourtListDown(int courtId, String courtName) {
        this.courtId = courtId;
        this.courtName = courtName;
    }

    public int getCourtId() {
        return courtId;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    @NonNull
    @Override
    public String toString() {
        return courtName; // This will be displayed in the Spinner
    }
}
