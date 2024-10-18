package Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Models.Reply;
import SqliteHelper.Sqlite;

public class ReplyRepository {
    private Sqlite dbHelper;

    // Constructor
    public ReplyRepository(Context context) {
        dbHelper = new Sqlite(context);
    }

    public long insertReply(int reviewId, int userId, String content, String createdAt, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("review_id", reviewId);
        values.put("user_id", userId);
        values.put("content", content);
        values.put("create_at", createdAt);
        values.put("status", status);

        long result = db.insert("Reply", null, values);
        db.close();
        return result;
    }

    public List<Reply> getAllRepliesByReviewId(int reviewId) {
        List<Reply> replies = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("Reply", null, "review_id = ?", new String[]{String.valueOf(reviewId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int replyId = cursor.getInt(cursor.getColumnIndexOrThrow("reply_id"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("create_at"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                Reply reply = new Reply(replyId, reviewId, userId, content, createdAt, status);
                replies.add(reply);
            }
            cursor.close();
        }

        db.close();

        replies.sort((r1, r2) -> r2.getCreateAt().compareTo(r1.getCreateAt()));
        return replies;
    }
}
