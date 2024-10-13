package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Models.User;
import SqliteHelper.Sqlite;

public class UserRepository {
    private Sqlite dbHelper;

    public UserRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public User getUserByPhoneNumber(String phoneNumber){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM User WHERE phone = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phoneNumber});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow("date_created"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1;

            user = new User(userId, password, fullName, email, phone, dateCreated, isActive);
        }

        cursor.close();
        db.close();
        return user;
    }

    public long insertUser(String password, String fullName, String email, String phone, String dateCreated, int isActive) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("password", password);
        values.put("full_name", fullName);
        values.put("email", email);
        values.put("phone", phone);
        values.put("date_created", dateCreated);
        values.put("is_active", isActive);

        long result = db.insert("User", null, values);
        db.close();
        return result;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("password", user.getPassword());
        values.put("full_name", user.getFullName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("date_created", user.getDateCreated());
        values.put("is_active", user.isActive());

        String selection = "user_id = ?";
        String[] selectionArgs = { String.valueOf(user.getUserId()) };

        int rowsUpdated = db.update("User", values, selection, selectionArgs);
        db.close();
        return rowsUpdated;
    }

    // Method to retrieve all users
    public List<User> getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<User> userList = new ArrayList<>();

        // Query to select all users
        String query = "SELECT * FROM User";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow("date_created"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1;

                // Create a new User object
                User user = new User(userId, password, fullName, email, phone, dateCreated, isActive);
                // Add user to the list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }


}
