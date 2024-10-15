package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Models.Booking;
import SqliteHelper.Sqlite;

public class BookingRepository {

    private Sqlite dbHelper;

    // Constructor
    public BookingRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public long insertBooking(int courtId, int playerId, String bookingDate, String startTime, String endTime, float price, String status, String createdAt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

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

    public List<Booking> getBookingsByCourtIdAndDate(int courtId, String bookingDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();

        // SQL query to select bookings based on courtId and bookingDate
        String query = "SELECT * FROM Booking WHERE court_id = ? AND booking_date = ? AND status != ?";
        String[] selectionArgs = { String.valueOf(courtId), bookingDate, "CANCEL" };

        // Execute the query and fetch results
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Iterate through the result set and create Booking objects
        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                        cursor.getInt(cursor.getColumnIndexOrThrow("booking_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("court_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("player_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("booking_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("start_time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("end_time")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookings;
    }

    public List<Booking> getBookingsByPlayerIdAndDate(int playerId, String bookingDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();

        // Base query
        String query = "SELECT * FROM Booking WHERE player_id = ?";
        List<String> selectionArgsList = new ArrayList<>();
        selectionArgsList.add(String.valueOf(playerId));

        // Check if bookingDate is provided
        if (bookingDate != null && !bookingDate.isEmpty()) {
            query += " AND booking_date = ?";
            selectionArgsList.add(bookingDate);
        }

        // Convert list to array
        String[] selectionArgs = selectionArgsList.toArray(new String[0]);

        // Execute the query and fetch results
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Iterate through the result set and create Booking objects
        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                        cursor.getInt(cursor.getColumnIndexOrThrow("booking_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("court_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("player_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("booking_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("start_time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("end_time")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookings;
    }

    public Booking getBookingsById(int bookingId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Booking booking = null;

        // SQL query to select bookings based on courtId and bookingDate
        String query = "SELECT * FROM Booking WHERE booking_id = ? ";
        String[] selectionArgs = { String.valueOf(bookingId)};

        // Execute the query and fetch results
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Iterate through the result set and create Booking objects
        if (cursor.moveToFirst()) {
            do {
                booking = new Booking(
                        cursor.getInt(cursor.getColumnIndexOrThrow("booking_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("court_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("player_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("booking_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("start_time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("end_time")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                );
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return booking;
    }

    public int updateBooking(int bookingId, int courtId, int playerId, String bookingDate, String startTime, String endTime, float price, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("court_id", courtId);
        values.put("player_id", playerId);
        values.put("booking_date", bookingDate);
        values.put("start_time", startTime);
        values.put("end_time", endTime);
        values.put("price", price);
        values.put("status", status);

        // Updating row in the Booking table
        int rowsAffected = db.update("Booking", values, "booking_id = ?", new String[]{String.valueOf(bookingId)});
        db.close();
        return rowsAffected; // Returns the number of rows affected by the update
    }



}
