package SqliteHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Sqlite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CourtBookingSystem.db";
    private static final int DATABASE_VERSION = 1;

    public Sqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;"); // Enable foreign keys

        db.execSQL("CREATE TABLE Booking (" +
                "booking_id INTEGER PRIMARY KEY, " +
                "court_id INTEGER, " +
                "player_id INTEGER, " +
                "booking_date TEXT, " +
                "start_time TEXT, " +
                "end_time TEXT, " +
                "price REAL, " +
                "status TEXT, " +
                "created_at TEXT, " +
                "FOREIGN KEY(court_id) REFERENCES Court(court_id), " +
                "FOREIGN KEY(player_id) REFERENCES User(user_id))");

        db.execSQL("CREATE TABLE User (" +
                "user_id INTEGER PRIMARY KEY, " +
                "user_name TEXT, " +
                "full_name TEXT, " +
                "email TEXT, " +
                "phone TEXT, " +
                "role TEXT, " +
                "date_created TEXT, " +
                "is_active INTEGER)");

        db.execSQL("CREATE TABLE Court (" +
                "court_id INTEGER PRIMARY KEY, " +
                "court_owner_id INTEGER, " +
                "court_name TEXT, " +
                "location TEXT, " +
                "address TEXT, " +
                "status TEXT, " +
                "sport_id INTEGER, " +
                "FOREIGN KEY(court_owner_id) REFERENCES User(user_id), " +
                "FOREIGN KEY(sport_id) REFERENCES Sport(sport_id))");

        db.execSQL("CREATE TABLE Review (" +
                "review_id INTEGER PRIMARY KEY, " +
                "booking_id INTEGER, " +
                "user_id INTEGER, " +
                "court_id INTEGER, " +
                "rating INTEGER, " +
                "content TEXT, " +
                "create_at TEXT, " +
                "status TEXT, " +
                "FOREIGN KEY(booking_id) REFERENCES Booking(booking_id), " +
                "FOREIGN KEY(user_id) REFERENCES User(user_id), " +
                "FOREIGN KEY(court_id) REFERENCES Court(court_id))");

        db.execSQL("CREATE TABLE Sport (" +
                "sport_id INTEGER PRIMARY KEY, " +
                "sport_name TEXT)");

        db.execSQL("CREATE TABLE CourtSlot (" +
                "court_slot_id INTEGER PRIMARY KEY, " +
                "court_id INTEGER, " +
                "time_start TEXT, " +
                "time_end TEXT, " +
                "cost REAL, " +
                "FOREIGN KEY(court_id) REFERENCES Court(court_id))");

        db.execSQL("CREATE TABLE UserSport (" +
                "sport_id INTEGER, " +
                "user_id INTEGER, " +
                "level TEXT, " +
                "PRIMARY KEY(sport_id, user_id), " +
                "FOREIGN KEY(sport_id) REFERENCES Sport(sport_id), " +
                "FOREIGN KEY(user_id) REFERENCES User(user_id))");

        db.execSQL("CREATE TABLE Reply (" +
                "reply_id INTEGER PRIMARY KEY, " +
                "review_id INTEGER, " +
                "content TEXT, " +
                "create_at TEXT, " +
                "status TEXT, " +
                "FOREIGN KEY(review_id) REFERENCES Review(review_id))");

        db.execSQL("CREATE TABLE SlotBooking (" +
                "booking_id INTEGER, " +
                "court_slot_id INTEGER, " +
                "status TEXT, " +
                "PRIMARY KEY(booking_id, court_slot_id), " +
                "FOREIGN KEY(booking_id) REFERENCES Booking(booking_id), " +
                "FOREIGN KEY(court_slot_id) REFERENCES CourtSlot(court_slot_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Booking");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Court");
        db.execSQL("DROP TABLE IF EXISTS Review");
        db.execSQL("DROP TABLE IF EXISTS Sport");
        db.execSQL("DROP TABLE IF EXISTS CourtSlot");
        db.execSQL("DROP TABLE IF EXISTS UserSport");
        db.execSQL("DROP TABLE IF EXISTS Reply");
        db.execSQL("DROP TABLE IF EXISTS SlotBooking");
        onCreate(db);
    }
}
