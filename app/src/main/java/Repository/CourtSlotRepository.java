package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Models.CourtSlot;
import SqliteHelper.Sqlite;

public class CourtSlotRepository {

    private Sqlite dbHelper;

    // Constructor
    public CourtSlotRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public long insertCourtSlot(int courtId, String timeStart, String timeEnd, float cost) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("court_id", courtId);
        values.put("time_start", timeStart);
        values.put("time_end", timeEnd);
        values.put("cost", cost);

        long result = db.insert("CourtSlot", null, values);
        db.close();
        return result;
    }

    public List<CourtSlot> getSlotsByCourtId(int courtId) {
        List<CourtSlot> courtSlotList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the query to select slots by courtId
        String query = "SELECT * FROM CourtSlot WHERE court_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courtId)});

        // Loop through the results
        if (cursor.moveToFirst()) {
            do {
                // Create a new CourtSlot object for each row in the result
                CourtSlot courtSlot = null;
                int courtSlotId = cursor.getInt(cursor.getColumnIndexOrThrow("court_slot_id"));
                String timeStart = cursor.getString(cursor.getColumnIndexOrThrow("time_start"));
                String timeEnd = cursor.getString(cursor.getColumnIndexOrThrow("time_end"));
                double cost = cursor.getDouble(cursor.getColumnIndexOrThrow("cost"));

                courtSlot = new CourtSlot(courtSlotId, courtId, timeStart, timeEnd, cost);
                // Add the CourtSlot object to the list
                courtSlotList.add(courtSlot);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return courtSlotList;
    }

    public CourtSlot getSlotById(int courtSlotId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the query to select slots by courtId
        String query = "SELECT * FROM CourtSlot WHERE court_slot_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courtSlotId)});


                CourtSlot courtSlot = null;
                int courtId = cursor.getInt(cursor.getColumnIndexOrThrow("court_id"));
                String timeStart = cursor.getString(cursor.getColumnIndexOrThrow("time_start"));
                String timeEnd = cursor.getString(cursor.getColumnIndexOrThrow("time_end"));
                double cost = cursor.getDouble(cursor.getColumnIndexOrThrow("cost"));

                courtSlot = new CourtSlot(courtSlotId, courtId, timeStart, timeEnd, cost);
                // Add the CourtSlot object to the list


        // Close the cursor and database connection
        cursor.close();
        db.close();

        return courtSlot;
    }

    public int updateCourtSlot(int courtSlotId, int courtId, String timeStart, String timeEnd, double cost) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the new values for the court slot
        values.put("court_id", courtId);
        values.put("time_start", timeStart);
        values.put("time_end", timeEnd);
        values.put("cost", cost);

        // Update the row where court_slot_id matches
        int rowsAffected = db.update("CourtSlot", values, "court_slot_id = ?", new String[]{String.valueOf(courtSlotId)});
        db.close();

        return rowsAffected; // Returns the number of rows affected
    }



}
