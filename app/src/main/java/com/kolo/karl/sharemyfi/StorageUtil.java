package com.kolo.karl.sharemyfi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    public static final int DELETE_OK = 0;
    public static final int ADDED_GENERIC_ERROR = -1;
    public static final int ADD_INVALID_SSID_OR_PASSWORD = -2;
    public static final int DELETE_GENERIC_ERROR = -1;

    public StorageUtil(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String cmd = "CREATE TABLE " + WifiInfoContract.InfoEntry.TABLE_NAME +
                "( " + WifiInfoContract.InfoEntry._ID + " INTEGER PRIMARY KEY," +
                WifiInfoContract.InfoEntry.SSID + " TEXT NOT NULL UNIQUE," +
                WifiInfoContract.InfoEntry.PASS + " TEXT NOT NULL," +
                WifiInfoContract.InfoEntry.SALT + " TEXT NOT NULL )";
        try
        {
            sqLiteDatabase.execSQL(cmd);
        } catch (SQLException e)
        {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    /*
    Saves Wifi information from user into databes

    TODO: need to bind variables
    TODO: secure passwords
     */
    public int addWifiInfo(String ssid, String pass)
    {
        if (ssid.isEmpty() || pass.isEmpty())
            return ADD_INVALID_SSID_OR_PASSWORD;

        ContentValues values = new ContentValues();
        values.put(WifiInfoContract.InfoEntry.SSID, ssid);
        values.put(WifiInfoContract.InfoEntry.PASS, pass);
        values.put(WifiInfoContract.InfoEntry.SALT, pass);

        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insertWithOnConflict(WifiInfoContract.InfoEntry.TABLE_NAME,
                null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "ID: " + id);

        if (id == DB_ERROR)
            return ADDED_GENERIC_ERROR;
        else
            return ADDED_OK;
    }

    public int deleteSSID(String ssid)
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = WifiInfoContract.InfoEntry.SSID + "=?";
        String[] params = { ssid };
        int ret = db.delete(WifiInfoContract.InfoEntry.TABLE_NAME, query, params);

        if (ret == 1) // we expect only 1 row affected
            return DELETE_OK;
        else
            return DELETE_GENERIC_ERROR;
    }

    // returns info of an SSID
    public Cursor getSSID(String ssid)
    {
        String[] columns = {
                WifiInfoContract.InfoEntry.PASS,
                WifiInfoContract.InfoEntry._ID
        };
        String query = WifiInfoContract.InfoEntry.SSID + "=?";
        String[] params = { ssid };
        SQLiteDatabase db = getReadableDatabase();

        return db.query(WifiInfoContract.InfoEntry.TABLE_NAME,
                columns,
                query, params, null, null, null);
    }

    public Cursor getSSIDs()
    {
        String[] projection = {
            WifiInfoContract.InfoEntry.SSID,
            WifiInfoContract.InfoEntry._ID
        };
        String sortOrder = WifiInfoContract.InfoEntry.SSID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();

        return db.query(WifiInfoContract.InfoEntry.TABLE_NAME,
                        projection,
                        null, null, null, null, sortOrder);
    }
}
