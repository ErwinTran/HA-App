package com.etran.ergon.hausaufgabenapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ImageButton btNew;
    private TextView description;
    private MyArrayAdapter myArrayAdapter;
    private ArrayList<Entry> entries;
    private SharedPreferences spTasks;
    private SharedPreferences spSettings;

    private final int NEW_RESULT_CODE = 1;
    private final int EDIT_RESULT_CODE = 2;
    private final int SETTINGS_RESULT_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);
        spTasks = getSharedPreferences("Tasks", Context.MODE_PRIVATE);
        spSettings = getSharedPreferences("Setting", Context.MODE_PRIVATE);

        initComponents();
    }

    private void initComponents() {
        description = (TextView) findViewById(R.id.description);
        description.setBackgroundResource(R.drawable.edittext_bg);
        listView = (ListView) findViewById(R.id.listView);

        // get all saved entry and add them to entries
        entries = new ArrayList<>();
        Map<String, ?> keys = spTasks.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Entry e = Entry.create(entry.getValue().toString());
            entries.add(e);
        }

        myArrayAdapter = new MyArrayAdapter(this, entries);
        listView.setAdapter(myArrayAdapter);

        // new button
        btNew = (ImageButton) findViewById(R.id.btNew);
        btNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewIntent = new Intent(ListActivity.this, NewActivity.class);
                startNewIntent.putExtra("toEdit", false);

                String serializedData = spSettings.getString("setting", null);
                if (serializedData != null) {
                    Setting setting = Setting.create(serializedData);
                    startNewIntent.putExtra("setting", setting.serialize());
                }
                startActivityForResult(startNewIntent, NEW_RESULT_CODE);
            }
        });
    }

    public void clickItem(Entry entry) {
        // print description in the textblock down below
        description.setText(entry.getDescription());
    }

    public void longClickItem(Entry entry) {
        // edit an entry
        Intent intent = new Intent(ListActivity.this, NewActivity.class);
        intent.putExtra("toEdit", true);

        String serializedData = spSettings.getString("setting", null);
        if (serializedData != null) {
            Setting setting = Setting.create(serializedData);
            intent.putExtra("setting", setting.serialize());
        }
        intent.putExtra("entry", entry.serialize());
        intent.putExtra("index", entries.indexOf(entry));
        startActivityForResult(intent, EDIT_RESULT_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mDelete = menu.add("Löschen");
        mDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (myArrayAdapter.getCount() != 0) {
                    ArrayList<Entry> toDeleteList = new ArrayList<>();
                    SharedPreferences.Editor editor = spTasks.edit();

                    for (Entry e : myArrayAdapter.getAllItems()) {
                        if (e.toDelete()) {
                            toDeleteList.add(e);
                        }
                    }
                    for (Entry e : toDeleteList) {
                        myArrayAdapter.remove(e);
                        editor.remove(e.getKey());
                    }

                    editor.apply();
                    return true;
                } else {
                    Toast.makeText(ListActivity.this,
                            "Es gibt nichts zum löschen", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        MenuItem mSettings = menu.add("Einstellungen");
        mSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // open new activity
                Intent intent = new Intent(ListActivity.this, SettingsActivity.class);

                String serializedData = spSettings.getString("setting", null);
                if (serializedData != null) {
                    Setting setting = Setting.create(serializedData);
                    intent.putExtra("setting", setting.serialize());
                }

                startActivityForResult(intent, SETTINGS_RESULT_CODE);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                onAddEntryActivity(data);
            }
        }
        else if(requestCode == EDIT_RESULT_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                onOverwriteEntryActivity(data);
            }
        }
        else if(requestCode == SETTINGS_RESULT_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                onSettingsActivity(data);
            }
        }
    }

    private void onAddEntryActivity(Intent data) {
        // get data from the other activity
        String serializedData = data.getStringExtra("entry");

        Entry entry = Entry.create(serializedData);
        myArrayAdapter.add(entry);

        // add e to shared preferences
        SharedPreferences.Editor editor = spTasks.edit();
        editor.putString(entry.getKey(), serializedData);
        editor.apply();
    }

    private void onOverwriteEntryActivity(Intent data) {
        // rewrite the entry
        String serializedData = data.getStringExtra("entry");

        Entry entry = entries.get(data.getIntExtra("index", -1));

        SharedPreferences.Editor editor = spTasks.edit();
        editor.remove(entry.getKey());

        myArrayAdapter.remove(entry);
        entry = Entry.create(serializedData);
        myArrayAdapter.add(entry);

        editor.putString(entry.getKey(), serializedData);
        editor.apply();
    }

    private void onSettingsActivity(Intent data) {
        SharedPreferences.Editor editor = spSettings.edit();
        editor.clear();
        String serializedData = data.getStringExtra("setting");
        editor.putString("setting", serializedData);
        editor.apply();
    }
}