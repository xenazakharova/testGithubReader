package com.xenazakharova.githubapi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "github_info";
 
    private static final int DATABASE_VERSION = 1;
 
    // запрос на создание базы данных
    private static final String DATABASE_CREATE = 
    		"create table users (_id integer primary key autoincrement, "
    				+ "user_id integer not null, "
    				+ "login text not null, "
    				+ "name text, "
    				+ "company text, "
    				+ "avatar_url text, "
    				+ "followers integer, "
    				+ "following integer "
    				+");";

 
 
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
 
    // метод вызывается при обновлении базы данных, например, когда вы увеличиваете номер версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
            int newVersion) {
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS events");
        onCreate(database);
    }
}
