package Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String KEY_USER_ID = "user_id";

    private static final String KEY_COURT_OWNER_ID = "court_owner_id";

    public SessionManager(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }


    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);  // Returns -1 if no userId is found
    }


    public void clearSession() {
        editor.clear();
        editor.apply();
    }


    public boolean isLoggedInUser() {
        return getUserId() != -1;
    }

    public void saveCourtOwnerId(int userId) {
        editor.putInt(KEY_COURT_OWNER_ID, userId);
        editor.apply();
    }


    public int getCourtOwnerId() {
        return sharedPreferences.getInt(KEY_COURT_OWNER_ID, -1);  // Returns -1 if no userId is found
    }


    public boolean isLoggedInCourtOwner() {
        return getCourtOwnerId() != -1;
    }

}
