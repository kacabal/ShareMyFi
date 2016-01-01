package com.kolo.karl.sharemyfi;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddWifi extends AppCompatActivity {

    public static String ADD_NEW_WIFI_INFO = "com.kolo.karl.sharemyfi.AddNewWifiInfo";
    public static final String TAG = "AddWifi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ID_ACTION_ADD_TOOLBAR);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));

        final Context ctx = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ID_ACTION_SAVE_WIFI_INFO);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ssidInput = (EditText)findViewById(R.id.ID_SSID_INPUT);
                EditText passInput = (EditText)findViewById(R.id.ID_PASS_INPUT);

                StorageUtil storageUtil = new StorageUtil(ctx);
                if (storageUtil.addWifiInfo(ssidInput.getText().toString(),
                        passInput.getText().toString()) == StorageUtil.ADDED_OK)
                {
                    String toastMsg = getString(R.string.TXT_ADDED_COLON) + " " + ssidInput.getText();
                    Toast.makeText(AddWifi.this, toastMsg, Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }

}
