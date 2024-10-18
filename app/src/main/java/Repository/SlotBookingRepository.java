package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import SqliteHelper.Sqlite;

public class SlotBookingRepository {

    private Sqlite dbHelper;

    // Constructor
    public SlotBookingRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public long insertSlotBooking(int bookingId, int courtSlotId, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("booking_id", bookingId);
        values.put("court_slot_id", courtSlotId);
        values.put("status", status);

        long result = db.insert("SlotBooking", null, values);
        db.close();
        return result;
    }

}
