package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

}
