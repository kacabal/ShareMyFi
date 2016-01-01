package com.kolo.karl.sharemyfi;

import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by karl on 12/29/15.
 */
public class WifiInfoContract
{
    public static final String TAG = "WifiInfoContract";
    public WifiInfoContract() {}

    public static abstract class InfoEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "wifiEntries";
        public static final String SSID = "ssid";
        public static final String PASS = "pass";
        public static final String SALT = "salt";
    }

    public String[] getColumnNames()
    {
        String[] names = {InfoEntry.SSID, InfoEntry.PASS, InfoEntry.SALT};
        return names;
    }

    public String getColumnType(String columnName)
    {
        if (columnName.equals(InfoEntry.SSID) ||
            columnName.equals(InfoEntry.PASS) ||
            columnName.equals(InfoEntry.SALT))
        {
            return "TEXT";
        }
        else
        {
            Log.d(TAG, "Unhandled column name: " + columnName);
            return "";
        }
    }
}
