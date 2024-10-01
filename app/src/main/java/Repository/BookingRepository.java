package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import SqliteHelper.Sqlite;

public class BookingRepository {

    private Sqlite dbHelper;

    // Constructor
    public BookingRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public long insertBooking(int bookingId, int courtId, int playerId, String bookingDate, String startTime, String endTime, float price, String status, String createdAt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("booking_id", bookingId);
        values.put("court_id", courtId);
        values.put("player_id", playerId);
        values.put("booking_date", bookingDate);
        values.put("start_time", startTime);
        values.put("end_time", endTime);
        values.put("price", price);
        values.put("status", status);
        values.put("created_at", createdAt);

        long result = db.insert("Booking", null, values);
        db.close();
        return result;
    }

}
