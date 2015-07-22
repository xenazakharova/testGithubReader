package com.xenazakharova.githubapi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseAdapter {
 
    // поля базы данных
    public static final String KEY_ROWID = "_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_NAME = "name";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_AVATAR_URL = "avatar_url";
    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_FOLLOWING = "following";
    private static final String DATABASE_TABLE = "users";

    private Context context;
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
 
    public DataBaseAdapter(Context context) {
        this.context = context;
    }
 
    public DataBaseAdapter open() throws SQLException {
        dbHelper = new DataBaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }
 
    public void close() {
        dbHelper.close();
    }
 
    public long insertUser(int users_id, String login, String name,String company, String avatar_url, int followers, int following){
        
    	ContentValues initialValues = createContentValues(users_id, login, name, company, avatar_url, followers, following);
    
        return database.insert(DATABASE_TABLE, null, initialValues);
    }
 
    /**
     * обновить список
     */
    public boolean updateUser(
    		long rowId, 
    		int users_id, 
    		String login,
    		String name,
    		String company,
    		String avatar_url,
    		int followers,
    		int following) {
    	
        ContentValues updateValues = createContentValues(users_id, login, name, company, avatar_url, followers, following);
 
        return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="+ rowId, null) > 0;
    }

    public boolean deleteUser(long rowId) {
        return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
 
    public Cursor fetchAllUsers() {
        return database.query(DATABASE_TABLE, 
        		new String[] { KEY_ROWID, KEY_USER_ID, KEY_LOGIN, KEY_COMPANY,
        		KEY_AVATAR_URL, KEY_FOLLOWERS, KEY_FOLLOWING}, null, null, null, null, null);
       }
 
    public Cursor fetchUser(int userId) throws SQLException {
        Cursor mCursor = database.query(true, DATABASE_TABLE,new String[] { KEY_ROWID, KEY_USER_ID, KEY_LOGIN, KEY_COMPANY,
        		KEY_AVATAR_URL, KEY_FOLLOWERS, KEY_FOLLOWING}, KEY_USER_ID + "=" + userId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
 
    private ContentValues createContentValues(int user_id, String login, String name, String company, String avatar_url, int followers, int following){
    	
        ContentValues values = new ContentValues();        
        
        values.put(KEY_USER_ID, user_id);
        values.put(KEY_LOGIN, login);
        values.put(KEY_COMPANY, company);
        values.put(KEY_AVATAR_URL, avatar_url);
        values.put(KEY_FOLLOWERS, followers);
        values.put(KEY_FOLLOWING, following);
      
        return values;
    }
    
}
