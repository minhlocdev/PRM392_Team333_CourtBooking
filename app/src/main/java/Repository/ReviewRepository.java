package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Models.Review;
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

    public List<Review> getAllReviewsByCourtId(int courtId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                "review_id",
                "booking_id",
                "user_id",
                "court_id",
                "rating",
                "content",
                "create_at",
                "status"
        };

        Cursor cursor = db.query("Review", columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Create a new Review object and set its fields
                int reviewId = cursor.getInt(cursor.getColumnIndexOrThrow("review_id"));
                int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow("booking_id"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                int rate = cursor.getInt(cursor.getColumnIndexOrThrow("rating"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String createAt = cursor.getString(cursor.getColumnIndexOrThrow("create_at"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                Review review = new Review(reviewId, bookingId, userId, courtId, rate, content, createAt, status);

                // Add the review to the list
                reviews.add(review);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        reviews.sort((r1, r2) -> r2.getCreateAt().compareTo(r1.getCreateAt()));

        return reviews;
    }

}
