package com.kolo.karl.sharemyfi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddWifi extends AppCompatActivity {

    public static String ADD_NEW_WIFI_INFO = "com.kolo.karl.sharemyfi.AddNewWifiInfo";
    public static String EDIT_WIFI_INFO = "com.kolo.karl.sharemyfi.EditWifiInfo";
    public static String EDIT_WIFI_NAME_EXTRA = "EditWifiInfo.Name";
    public static final String TAG = "AddWifi";
    private StorageUtil storageUtil = new StorageUtil(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

        final EditText ssidInput = (EditText)findViewById(R.id.ID_SSID_INPUT);
        final EditText passInput = (EditText)findViewById(R.id.ID_PASS_INPUT);
        final boolean isEditing = (getIntent().getAction() == EDIT_WIFI_INFO);

        if (isEditing)
        {
            setTitle(R.string.TXT_ACTION_EDIT_WIFI);

            String ssid = getIntent().getStringExtra(EDIT_WIFI_NAME_EXTRA);
            ssidInput.setText(ssid);

            Cursor wifiInfo = storageUtil.getSSID(ssid);
            wifiInfo.moveToFirst();
            String pass = wifiInfo.getString(0); // first column is password
            wifiInfo.close();
            passInput.setText(pass);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.ID_ACTION_ADD_TOOLBAR);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ID_ACTION_SAVE_WIFI_INFO);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newSsid = ssidInput.getText().toString();
                if (storageUtil.addWifiInfo(newSsid,
                                            passInput.getText().toString()) == StorageUtil.ADDED_OK)
                {
                    String toastMsg = getString(R.string.TXT_SAVED_COLON) + " " + ssidInput.getText();
                    Toast.makeText(AddWifi.this, toastMsg, Toast.LENGTH_SHORT).show();

                    String oldSsid = getIntent().getStringExtra(EDIT_WIFI_NAME_EXTRA);
                    if (isEditing && !oldSsid.equals(newSsid))
                    {
                        // user edited the SSID, delete stale entry
                        storageUtil.deleteSSID(oldSsid);
                    }

                    sendBroadcast(new Intent(ManageWifiInfo.WIFI_INFO_ADDED));
                    finish();
                }
                else
                {
                    AlertDialog err = new AlertDialog.Builder(AddWifi.this).create();
                    err.setTitle(R.string.TXT_ADD_FAILED_TITLE);
                    err.setMessage(getApplication().getString(R.string.TXT_ADD_FAILED));
                    err.setButton(AlertDialog.BUTTON_POSITIVE,
                            getApplication().getText(R.string.TXT_OK),
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    finish();
                                }
                            });
                    err.show();
                }
            }
        });
    }

}
