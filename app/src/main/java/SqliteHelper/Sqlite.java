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

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys = ON;"); // Enable foreign keys

        db.execSQL("CREATE TABLE Booking (" +
                "booking_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "password TEXT, " +
                "full_name TEXT, " +
                "email TEXT, " +
                "phone TEXT, " +
                "date_created TEXT, " +
                "is_active INTEGER)");

        db.execSQL("CREATE TABLE CourtOwner (" +
                        "court_owner_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "password TEXT, " +
                        "full_name TEXT, " +
                        "email TEXT, " +
                        "phone TEXT, " +
                        "tax_code TEXT, " +
                        "date_created TEXT, " +
                        "is_active INTEGER)");

        db.execSQL("CREATE TABLE Court (" +
                "court_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "court_owner_id INTEGER, " +
                "court_name TEXT, " +
                "open_time TEXT," +
                "closed_time TEXT," +
                "province TEXT, " +
                "address TEXT, " +
                "image BLOB," +
                "status TEXT, " +
                "FOREIGN KEY(court_owner_id) REFERENCES CourtOwner(court_owner_id))");

        db.execSQL("CREATE TABLE Review (" +
                "review_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

        db.execSQL("CREATE TABLE CourtSlot (" +
                "court_slot_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "court_id INTEGER, " +
                "time_start TEXT, " +
                "time_end TEXT, " +
                "cost REAL, " +
                "FOREIGN KEY(court_id) REFERENCES Court(court_id))");

        db.execSQL("CREATE TABLE Reply (" +
                "reply_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insert default users
        db.execSQL("INSERT INTO User (user_id, password, full_name, email, phone, date_created, is_active) " +
                "VALUES (1, '123', 'John Doe', 'john@example.com', '123456789', '2024-10-01', 1)");
        db.execSQL("INSERT INTO User (user_id, password, full_name, email, phone, date_created, is_active) " +
                "VALUES (2, '123', 'Jane Doe', 'jane@example.com', '987654321', '2024-10-02', 1)");
        db.execSQL("INSERT INTO User (user_id, password, full_name, email, phone, date_created, is_active) " +
                "VALUES (3, '123', 'Sam Smith', 'sam@example.com', '543216789', '2024-10-03', 1)");

        // Insert default courts
        db.execSQL("INSERT INTO Court (court_id, court_owner_id, court_name, open_time, closed_time, province, address, image, status) " +
                "VALUES (1, 1, 'City Tennis Court', '08:00', '21:00', 'City Center', '123 Main St',null, 'available')");
        db.execSQL("INSERT INTO Court (court_id, court_owner_id, court_name, open_time, closed_time, province, address, image, status) " +
                "VALUES (2, 1, 'Football Arena', '08:00', '21:00', 'Uptown', '456 High St', null, 'available')");
        db.execSQL("INSERT INTO Court (court_id, court_owner_id, court_name, open_time, closed_time, province, address, image, status) " +
                "VALUES (3, 1, 'Basketball Court', '08:00', '21:00', 'Suburb', '789 Park Ave', null, 'available')");

        // Insert default bookings
        db.execSQL("INSERT INTO Booking (booking_id, court_id, player_id, booking_date, start_time, end_time, price, status, created_at) " +
                "VALUES (1, 1, 1, '2024-10-01', '10:00', '11:00', 50.0, 'confirmed', '2024-10-01')");
        db.execSQL("INSERT INTO Booking (booking_id, court_id, player_id, booking_date, start_time, end_time, price, status, created_at) " +
                "VALUES (2, 2, 2, '2024-10-02', '12:00', '13:00', 70.0, 'confirmed', '2024-10-02')");
        db.execSQL("INSERT INTO Booking (booking_id, court_id, player_id, booking_date, start_time, end_time, price, status, created_at) " +
                "VALUES (3, 3, 3, '2024-10-03', '14:00', '15:00', 60.0, 'confirmed', '2024-10-03')");

        // Insert default court slots
        db.execSQL("INSERT INTO CourtSlot (court_slot_id, court_id, time_start, time_end, cost) " +
                "VALUES (1, 1, '10:00', '11:00', 30.0)");
        db.execSQL("INSERT INTO CourtSlot (court_slot_id, court_id, time_start, time_end, cost) " +
                "VALUES (2, 2, '12:00', '13:00', 40.0)");
        db.execSQL("INSERT INTO CourtSlot (court_slot_id, court_id, time_start, time_end, cost) " +
                "VALUES (3, 3, '14:00', '15:00', 50.0)");

        // Insert default slot bookings
        db.execSQL("INSERT INTO SlotBooking (booking_id, court_slot_id, status) " +
                "VALUES (1, 1, 'booked')");
        db.execSQL("INSERT INTO SlotBooking (booking_id, court_slot_id, status) " +
                "VALUES (2, 2, 'booked')");
        db.execSQL("INSERT INTO SlotBooking (booking_id, court_slot_id, status) " +
                "VALUES (3, 3, 'booked')");

        // Insert default reviews
        db.execSQL("INSERT INTO Review (review_id, booking_id, user_id, court_id, rating, content, create_at, status) " +
                "VALUES (1, 1, 1, 1, 5, 'Great experience!', '2024-10-01', 'approved')");
        db.execSQL("INSERT INTO Review (review_id, booking_id, user_id, court_id, rating, content, create_at, status) " +
                "VALUES (2, 2, 2, 2, 4, 'Good but expensive', '2024-10-02', 'pending')");
        db.execSQL("INSERT INTO Review (review_id, booking_id, user_id, court_id, rating, content, create_at, status) " +
                "VALUES (3, 3, 3, 3, 3, 'Decent court', '2024-10-03', 'pending')");

        // Insert default replies
        db.execSQL("INSERT INTO Reply (reply_id, review_id, content, create_at, status) " +
                "VALUES (1, 1, 'Thank you for your feedback!', '2024-10-02', 'approved')");
        db.execSQL("INSERT INTO Reply (reply_id, review_id, content, create_at, status) " +
                "VALUES (2, 2, 'We will improve our pricing', '2024-10-03', 'pending')");
        db.execSQL("INSERT INTO Reply (reply_id, review_id, content, create_at, status) " +
                "VALUES (3, 3, 'Thanks for your review', '2024-10-04', 'approved')");

        // Insert default court owner
        db.execSQL("INSERT INTO CourtOwner (password, full_name, email, phone, tax_code, date_created, is_active) " +
                "VALUES ('12345', 'John Doe', 'john.doe@example.com', '1234567890', 'TAX123456', '2024-10-03', 1)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Booking");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS CourtOwner");
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
