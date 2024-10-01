package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import SqliteHelper.Sqlite;

public class ReviewRepository {

    private Sqlite dbHelper;

    // Constructor
    public ReviewRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public long insertReview(int reviewId, int bookingId, int userId, int courtId, int rating, String content, String createdAt, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("review_id", reviewId);
        values.put("booking_id", bookingId);
        values.put("user_id", userId);
        values.put("court_id", courtId);
        values.put("rating", rating);
        values.put("content", content);
        values.put("create_at", createdAt);
        values.put("status", status);

        long result = db.insert("Review", null, values);
        db.close();
        return result;
    }

}
