package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

}
