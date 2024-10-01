package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Models.User;
import SqliteHelper.Sqlite;

public class UserRepository {
    private Sqlite dbHelper;

    // Constructor
    public UserRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public User getUserByPhoneNumber(String phoneNumber){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        // Query to get user by user_name
        String query = "SELECT * FROM User WHERE phone = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phoneNumber});

        if (cursor.moveToFirst()) {
            // If user is found, create User object
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow("date_created"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1;

            // Create a User object
            user = new User(userId, password, fullName, email, phone, role, dateCreated, isActive);
        }

        cursor.close();
        db.close();
        return user;
    }

    // Insert user into the database
    public long insertUser(String password, String fullName, String email, String phone, String role, String dateCreated, int isActive) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("password", password);
        values.put("full_name", fullName);
        values.put("email", email);
        values.put("phone", phone);
        values.put("role", role);
        values.put("date_created", dateCreated);
        values.put("is_active", isActive);

        long result = db.insert("User", null, values);
        db.close();
        return result;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Put the updated user details
        values.put("password", user.getPassword());
        values.put("full_name", user.getFullName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("role", user.getRole());
        values.put("date_created", user.getDateCreated());
        values.put("is_active", user.isActive());

        // Define the condition for updating the user with the specific user_id
        String selection = "user_id = ?";
        String[] selectionArgs = { String.valueOf(user.getUserId()) };

        // Perform the update and return the number of affected rows
        int rowsUpdated = db.update("User", values, selection, selectionArgs);
        db.close();
        return rowsUpdated;  // Returns the number of rows updated
    }



}
