package xact.idea.lecturepos.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private static final String mSharedName = "premo_prefs";
    public static final String TYPE_TOKEN = "token";
    public static final String TYPE_USER_ID = "userid";
    public static final String   USER_ID = "user";
    public static final String TYPE_USER_NAME = "username";
    public static final String TYPE_ADMIN = "username";
    public static final String USER_PIC = "pic";
    public static final String USER_EMAIL = "email";
    public static final String USER_SUNC_DATE_TIME = "syncdate";
    public static final String USER_SYNC = "sync";
    public static final String USER_ADDRESS = "address";
    public static void saveShared(Context c, String type, String val) {
        SharedPreferences.Editor ed = c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).edit();
        ed.putString(type, val);
        ed.commit();
    }


    public static void clearUserData(Context c) {
        SharedPreferences.Editor ed = c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).edit();
        //  ed.remove(TYPE_USER_LOGIN);

        ed.commit();

        SharedPreferences.Editor ed1 = c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).edit();
        ed1.clear();
        ed1.commit();
    }

    public static void removeShared(Context c, String type) {
        SharedPreferences.Editor ed = c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).edit();
        ed.remove(type);
        ed.commit();
    }

    public static String getShared(Context c, String type) {
        return c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).getString(type, "");
    }
    public static String getPic(Context c) {
        String val = getShared(c, USER_PIC);
        return val;
    }
    public static String getUserID(Context c) {
        String val = getShared(c, TYPE_USER_ID);
        return val;
    }public static String getUserName(Context c) {
        String val = getShared(c, TYPE_USER_NAME);
        return val;
    }
    public static String getEmail(Context c) {
        String val = getShared(c, USER_EMAIL);
        return val;
    }
    public static String getAdmin(Context c) {
        String val = getShared(c, TYPE_ADMIN);
        return val;
    }
    public static String getUser(Context c) {
        String val = getShared(c, USER_ID);
        return val;
    }
    public static String getSyncDateTime(Context c) {
        String val = getShared(c, USER_SUNC_DATE_TIME);
        return val;
    }
    public static String getSync(Context c) {
        String val = getShared(c, USER_SYNC);
        return val;
    }
    public static String getUserAddress(Context c) {
        String val = getShared(c, USER_ADDRESS);
        return val;
    }
}