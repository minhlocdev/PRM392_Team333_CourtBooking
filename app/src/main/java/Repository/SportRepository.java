package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import SqliteHelper.Sqlite;

public class SportRepository {

    private Sqlite dbHelper;

    // Constructor
    public SportRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public long insertSport(int sportId, String sportName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("sport_id", sportId);
        values.put("sport_name", sportName);

        long result = db.insert("Sport", null, values);
        db.close();
        return result;
    }
}
