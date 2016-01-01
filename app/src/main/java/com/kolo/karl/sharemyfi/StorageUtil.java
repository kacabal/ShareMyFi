package com.kolo.karl.sharemyfi;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by karl on 12/29/15.
 */
public class StorageUtil extends SQLiteOpenHelper
{
    public static final String TAG = "StorageUtil";

    // db information
    public static final int DB_VERSION = 1;
    public static final int DB_ERROR = -1;
    public static final String DB_NAME = "wifiInfo.db";

    // StorageUtil returns
    public static final int ADDED_OK = 0;
    public static final int ADDED_GENERIC_ERROR = -1;

    public StorageUtil(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String cmd = "CREATE TABLE " + WifiInfoContract.InfoEntry.TABLE_NAME +
                "( " + WifiInfoContract.InfoEntry._ID + " INTEGER PRIMARY KEY," +
                WifiInfoContract.InfoEntry.SSID + " TEXT NOT NULL," +
                WifiInfoContract.InfoEntry.PASS + " TEXT NOT NULL," +
                WifiInfoContract.InfoEntry.SALT + " TEXT NOT NULL )";
        try
        {
            sqLiteDatabase.execSQL(cmd);
        }
        catch (SQLException e)
        {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    public int addWifiInfo(String ssid, String pass)
    {
        ContentValues values = new ContentValues();
        values.put(WifiInfoContract.InfoEntry.SSID, ssid);
        values.put(WifiInfoContract.InfoEntry.PASS, pass);
        values.put(WifiInfoContract.InfoEntry.SALT, pass);

        long id = this.getWritableDatabase().insert(WifiInfoContract.InfoEntry.TABLE_NAME, null, values);
        Log.d(TAG, "ID: " + id);

        if (id == DB_ERROR)
            return ADDED_GENERIC_ERROR;
        else
            return ADDED_OK;
    }
}
