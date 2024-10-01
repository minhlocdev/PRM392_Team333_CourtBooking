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
                "role TEXT, " +
                "date_created TEXT, " +
                "is_active INTEGER)");

        db.execSQL("CREATE TABLE Court (" +
                "court_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "court_owner_id INTEGER, " +
                "court_name TEXT, " +
                "location TEXT, " +
                "address TEXT, " +
                "status TEXT, " +
                "sport_id INTEGER, " +
                "FOREIGN KEY(court_owner_id) REFERENCES User(user_id), " +
                "FOREIGN KEY(sport_id) REFERENCES Sport(sport_id))");

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

        db.execSQL("CREATE TABLE Sport (" +
                "sport_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sport_name TEXT)");

        db.execSQL("CREATE TABLE CourtSlot (" +
                "court_slot_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
        db.execSQL("INSERT INTO User (user_id, password, full_name, email, phone, role, date_created, is_active) " +
                "VALUES (1, '123', 'John Doe', 'john@example.com', '123456789', 'Player', '2024-10-01', 1)");
        db.execSQL("INSERT INTO User (user_id, password, full_name, email, phone, role, date_created, is_active) " +
                "VALUES (2, '123', 'Jane Doe', 'jane@example.com', '987654321', 'Admin', '2024-10-02', 1)");
        db.execSQL("INSERT INTO User (user_id, password, full_name, email, phone, role, date_created, is_active) " +
                "VALUES (3, '123', 'Sam Smith', 'sam@example.com', '543216789', 'Player', '2024-10-03', 1)");

        // Insert default sports
        db.execSQL("INSERT INTO Sport (sport_id, sport_name) VALUES (1, 'Tennis')");
        db.execSQL("INSERT INTO Sport (sport_id, sport_name) VALUES (2, 'Football')");
        db.execSQL("INSERT INTO Sport (sport_id, sport_name) VALUES (3, 'Basketball')");

        // Insert default courts
        db.execSQL("INSERT INTO Court (court_id, court_owner_id, court_name, location, address, status, sport_id) " +
                "VALUES (1, 1, 'City Tennis Court', 'City Center', '123 Main St', 'available', 1)");
        db.execSQL("INSERT INTO Court (court_id, court_owner_id, court_name, location, address, status, sport_id) " +
                "VALUES (2, 2, 'Football Arena', 'Uptown', '456 High St', 'available', 2)");
        db.execSQL("INSERT INTO Court (court_id, court_owner_id, court_name, location, address, status, sport_id) " +
                "VALUES (3, 2, 'Basketball Court', 'Suburb', '789 Park Ave', 'available', 3)");

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

        // Insert default user sports
        db.execSQL("INSERT INTO UserSport (sport_id, user_id, level) VALUES (1, 1, 'Advanced')");
        db.execSQL("INSERT INTO UserSport (sport_id, user_id, level) VALUES (2, 2, 'Intermediate')");
        db.execSQL("INSERT INTO UserSport (sport_id, user_id, level) VALUES (3, 3, 'Beginner')");
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
