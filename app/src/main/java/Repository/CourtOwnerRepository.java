package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Models.Court;
import Models.CourtOwner;
import Models.User;
import SqliteHelper.Sqlite;

public class CourtOwnerRepository {

    private Sqlite dbHelper;

    // Constructor
    public CourtOwnerRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public CourtOwner getCourtOwnerByPhoneNumber(String phoneNumber){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CourtOwner courtOwner = null;

        // Query to get court owner by phone
        String query = "SELECT * FROM CourtOwner WHERE phone = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phoneNumber});

        if (cursor.moveToFirst()) {
            // If court owner is found, create CourtOwner object
            int courtOwnerId = cursor.getInt(cursor.getColumnIndexOrThrow("court_owner_id"));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow("date_created"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1;
            String taxCode = cursor.getString(cursor.getColumnIndexOrThrow("tax_code"));

            // Create a User object
            courtOwner = new CourtOwner(courtOwnerId, password, fullName, email, phone, dateCreated, isActive, taxCode);
        }

        cursor.close();
        db.close();
        return courtOwner;
    }


    public long insertCourtOwner(String password, String fullName, String email, String phone, String dateCreated, int isActive, String taxCode) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("password", password);
        values.put("full_name", fullName);
        values.put("email", email);
        values.put("phone", phone);
        values.put("date_created", dateCreated);
        values.put("is_active", isActive);
        values.put("tax_code", taxCode);

        long result = db.insert("CourtOwner", null, values);
        db.close();
        return result;
    }

    public int updateCourtOwner(CourtOwner courtOwner) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("password", courtOwner.getPassword());
        values.put("full_name", courtOwner.getFullName());
        values.put("email", courtOwner.getEmail());
        values.put("phone", courtOwner.getPhone());
        values.put("date_created", courtOwner.getDateCreated());
        values.put("is_active", courtOwner.isActive());

        String selection = "court_owner_id = ?";
        String[] selectionArgs = { String.valueOf(courtOwner.getCourtOwnerId()) };

        int rowsUpdated = db.update("CourtOwner", values, selection, selectionArgs);
        db.close();
        return rowsUpdated;
    }

    public CourtOwner getCourtOwnerById(int courtOwnerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CourtOwner courtOwner = null;

        // Query to get court owner by court_owner_id
        String query = "SELECT * FROM CourtOwner WHERE court_owner_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courtOwnerId)});

        if (cursor.moveToFirst()) {
            // If court owner is found, create CourtOwner object
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow("date_created"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1;
            String taxCode = cursor.getString(cursor.getColumnIndexOrThrow("tax_code"));

            // Create a CourtOwner object
            courtOwner = new CourtOwner(courtOwnerId, password, fullName, email, phone, dateCreated, isActive, taxCode);
        }

        cursor.close();
        db.close();
        return courtOwner;
    }

    public List<Court> getCourtsByCourtOwnerId(int courtOwnerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Court> courts = new ArrayList<>();

        // Query to get courts by court owner ID
        String query = "SELECT * FROM Court WHERE court_owner_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courtOwnerId)});

        if (cursor.moveToFirst()) {
            do {

                int courtId = cursor.getInt(cursor.getColumnIndexOrThrow("court_id"));
                String courtName = cursor.getString(cursor.getColumnIndexOrThrow("court_name"));
                String province = cursor.getString(cursor.getColumnIndexOrThrow("province"));
                String openTime = cursor.getString(cursor.getColumnIndexOrThrow("open_time"));
                String closedTime = cursor.getString(cursor.getColumnIndexOrThrow("closed_time"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));


                // Create a Court object and add it to the list
                Court court = new Court(courtId, status, openTime, closedTime, address, province, courtName, courtOwnerId, image);
                courts.add(court);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return courts;
    }

}
