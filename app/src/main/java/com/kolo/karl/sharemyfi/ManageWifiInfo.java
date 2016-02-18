
package com.kolo.karl.sharemyfi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ManageWifiInfo extends AppCompatActivity {
    public static final String TAG = "ManageWifiInfo";
    public static final String MANAGE_WIFI_INFO = "com.kolo.karl.sharemyfi.ManageWifiInfo";
    private int _selectionCount = 0;
    ListView _infoListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _infoListView = (ListView)findViewById(R.id.ID_WIFI_LIST_MAIN);

        _infoListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ID_WIFI_INFO_CHECKBOX);
                checkBox.performClick();

                if (checkBox.isChecked())
                    _selectionCount++;
                else
                    _selectionCount--;

                toggleDeleteFabVisibility();
            }
        });

        findViewById(R.id.ID_ACTION_ADD).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent addNew = new Intent(AddWifi.ADD_NEW_WIFI_INFO);
                startActivity(addNew);
            }
        });

        findViewById(R.id.ID_ACTION_DELETE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getWindow().getContext());
                builder.setTitle(R.string.TXT_DELETE_ITEM_PROMPT_TITLE);
                builder.setMessage(R.string.TXT_DELETE_ITEM_PROMPT);
                builder.setPositiveButton(R.string.TXT_YES, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.TXT_NO, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        initItems();
    }

    private void initItems()
    {
        StorageUtil storageUtil = new StorageUtil(this);
        Cursor c = storageUtil.getSSIDs();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.wifi_info,
                c, new String[]{WifiInfoContract.InfoEntry.SSID},
                new int[]{R.id.ID_WIFI_INFO_CHECKBOX_LABEL},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        _infoListView.setAdapter(adapter);

    }

    private void toggleDeleteFabVisibility()
    {
        View deleteBtn = getWindow().findViewById(R.id.ID_ACTION_DELETE);
        if (_selectionCount <= 0)
        {
            _selectionCount = 0;
            deleteBtn.setVisibility(View.GONE);
        }
        else
        {
            deleteBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
