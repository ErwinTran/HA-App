package com.etran.ergon.hausaufgabenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private Spinner weekDay1;
    private Spinner weekDay2;
    private ListView lvClasses;
    private Button btCancel;
    private Button btSave;
    private Button btAdd;
    private Button btDelete;
    private ArrayAdapter<String> subjectsAdapter;
    private Setting setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            setting = Setting.create(b.getString("setting"));
        }
        else {
            setting = new Setting();
        }

        initComponents();
    }

    private void initComponents() {
        weekDay1 = (Spinner)findViewById(R.id.weekDay1);
        weekDay2 = (Spinner)findViewById(R.id.weekDay2);

        final ArrayAdapter<String> weekDaysAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getWeekDays());

        weekDay1.setAdapter(weekDaysAdapter);
        weekDay1.setSelection(setting.getDay1());
        weekDay1.setBackgroundResource(R.drawable.edittext_bg);
        weekDay2.setAdapter(weekDaysAdapter);
        weekDay2.setSelection(setting.getDay2());
        weekDay2.setBackgroundResource(R.drawable.edittext_bg);

        lvClasses = (ListView)findViewById(R.id.lvClasses);
        subjectsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, setting.getClasses());
        lvClasses.setAdapter(subjectsAdapter);

        btAdd = (Button)findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(SettingsActivity.this, subjectsAdapter);
                myDialog.show();
            }
        });

        btDelete = (Button)findViewById(R.id.btDelete);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete all marked subjects
                ArrayList<String> toDeleteList = new ArrayList<>();

                SparseBooleanArray checked = lvClasses.getCheckedItemPositions();
                for(int i = 0; i < subjectsAdapter.getCount(); i++) {
                    if(checked.get(i)) {
                        toDeleteList.add(subjectsAdapter.getItem(i));
                    }
                }
                for(String subject : toDeleteList) {
                    subjectsAdapter.remove(subject);
                }
                lvClasses.clearChoices();
            }
        });

        btCancel = (Button)findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });

        btSave = (Button)findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.setDay1(weekDay1.getSelectedItemPosition());
                setting.setDay2(weekDay2.getSelectedItemPosition());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("setting", setting.serialize());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @NonNull
    private ArrayList<String> getWeekDays() {
        ArrayList<String> weekDays = new ArrayList<>();
        weekDays.add("Montag");
        weekDays.add("Dienstag");
        weekDays.add("Mittwoch");
        weekDays.add("Donnerstag");
        weekDays.add("Freitag");
        weekDays.add("Samstag");
        weekDays.add("Sonntag");
        return weekDays;
    }
}
