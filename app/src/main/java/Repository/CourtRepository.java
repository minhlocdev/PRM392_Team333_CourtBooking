package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Models.Court;
import SqliteHelper.Sqlite;

public class CourtRepository {

    private Sqlite dbHelper;

    // Constructor
    public CourtRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public long insertCourt(int courtOwnerId, String courtName, String openTime, String closedTime, String province, String address, String status, byte[] image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("court_owner_id", courtOwnerId);
        values.put("court_name", courtName);
        values.put("open_time", openTime);
        values.put("closed_time", closedTime);
        values.put("province", province);
        values.put("address", address);
        values.put("status", status);
        values.put("image", image);

        long result = db.insert("Court", null, values);
        db.close();
        return result;
    }


    public Court getCourtById(int courtId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Court court = null;
        String query = "SELECT * FROM Court WHERE court_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courtId)});
        if (cursor.moveToFirst()) {
            // Retrieve data and construct a Court object
            int courtOwnerId = cursor.getInt(cursor.getColumnIndexOrThrow("court_owner_id"));
            String courtName = cursor.getString(cursor.getColumnIndexOrThrow("court_name"));
            String openTime = cursor.getString(cursor.getColumnIndexOrThrow("open_time"));
            String closedTime = cursor.getString(cursor.getColumnIndexOrThrow("closed_time"));
            String province = cursor.getString(cursor.getColumnIndexOrThrow("province"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

            court = new Court(courtId, status, openTime, closedTime, address, province, courtName, courtOwnerId, image);
        }

        cursor.close();
        db.close();

        return court;
    }

    public List<Court> getAllCourts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Court> courtList = new ArrayList<>();
        String query = "SELECT * FROM Court";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int courtId = cursor.getInt(cursor.getColumnIndexOrThrow("court_id"));
                int courtOwnerId = cursor.getInt(cursor.getColumnIndexOrThrow("court_owner_id"));
                String courtName = cursor.getString(cursor.getColumnIndexOrThrow("court_name"));
                String openTime = cursor.getString(cursor.getColumnIndexOrThrow("open_time"));
                String closedTime = cursor.getString(cursor.getColumnIndexOrThrow("closed_time"));
                String province = cursor.getString(cursor.getColumnIndexOrThrow("province"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

                Court court = new Court(courtId, status, openTime, closedTime, address, province, courtName, courtOwnerId, image);
                courtList.add(court);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courtList;
    }

}
