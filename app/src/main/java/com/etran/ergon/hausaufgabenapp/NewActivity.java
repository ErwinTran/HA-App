package com.etran.ergon.hausaufgabenapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewActivity extends AppCompatActivity {

    private AutoCompleteTextView subject; // school subject
    private EditText description;
    private EditText textDate;

    private final String myFormat = "EEE. dd. MMM. yyyy";
    private final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
    private Calendar myCalendar;

    private Button btCalendar;
    private Button btSave;
    private Button btCancel;

    private Setting setting;
    private Bundle b;

    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        b = getIntent().getExtras();
        if(b != null) {
            setting = Setting.create(b.getString("setting"));
        } else {
            setting = new Setting();
        }

        setting.setDay1(convertToWeekday(setting.getDay1()));
        setting.setDay2(convertToWeekday(setting.getDay2()));

        // get todays date
        myCalendar = Calendar.getInstance();

        initComponents();
    }

    private void initComponents() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, setting.getClasses());

        subject = (AutoCompleteTextView)findViewById(R.id.classes);
        subject.setAdapter(dataAdapter);
        subject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                subject.showDropDown();
            }
        });
        subject.setBackgroundResource(R.drawable.edittext_bg);

        description = (EditText)findViewById(R.id.description);
        description.setBackgroundResource(R.drawable.edittext_bg);

        textDate = (EditText)findViewById(R.id.editTextDate);
        textDate.setBackgroundResource(R.drawable.edittext_bg);
        setNextSchoolday(-1);

        // fill edittexts when edit
        if(b != null) {
            if(b.getBoolean("toEdit")) {
                Entry entry = Entry.create(b.getString("entry"));
                subject.setText(entry.getSubject());
                description.setText(entry.getDescription());
                myCalendar.setTime(entry.getDate());
                textDate.setText(sdf.format(myCalendar.getTime()));
            }
        }

        // calendar dialog
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        // calendar button
        btCalendar = (Button)findViewById(R.id.btCalendar);
        btCalendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // open dialog
                new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button btDay1 = (Button)findViewById(R.id.day1);
        btDay1.setText(getWeekDay(setting.getDay1()));
        btDay1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setNextSchoolday(setting.getDay1());
            }
        });

        Button btDay2 = (Button)findViewById(R.id.day2);
        btDay2.setText(getWeekDay(setting.getDay2()));
        btDay2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setNextSchoolday(setting.getDay2());
            }
        });

        // save button
        btSave = (Button)findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();

                Entry e = new Entry(subject.getText().toString(), description.getText().toString(), myCalendar.getTime());
                resultIntent.putExtra("entry", e.serialize());
                resultIntent.putExtra("index", b.getInt("index", -1));

                Toast.makeText(v.getContext(), "Saved a new entry", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btCancel = (Button)findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
    }

    private void updateLabel() {
        textDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void setNextSchoolday(int schoolDay) {

        // set mycalendar to the current day
        myCalendar = Calendar.getInstance();
        myCalendar.setFirstDayOfWeek(Calendar.TUESDAY);

        while (true) {
            int date = myCalendar.get(Calendar.DATE);
            int month = myCalendar.get(Calendar.MONTH);

            // next month
            if (date == getMonthLastDate(month, myCalendar.get(Calendar.YEAR))) {
                if (month == Calendar.DECEMBER) {
                    month = Calendar.JANUARY;
                    myCalendar.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR) + 1);
                }
                else {
                    month++;
                }
                myCalendar.set(Calendar.MONTH, month);
                date = 1;
            }
            else {
                date++;
            }
            myCalendar.set(Calendar.DATE, date);

            int dow = myCalendar.get(Calendar.DAY_OF_WEEK);
            if(schoolDay == -1) {
                if (dow == setting.getDay1() || dow == setting.getDay2()) {
                    break;
                }
            }
            else {
                if(dow == schoolDay) {
                    break;
                }
            }
        }
        updateLabel();
    }

    private static int getMonthLastDate(int month, int year) {

        switch(month) {
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;

            case Calendar.FEBRUARY:
                return year % 4 == 0 ? 29 : 28;

            default:
                return 31;
        }
    }

    private int convertToWeekday(int i) {
        switch(i) {
            case 0: return Calendar.MONDAY;
            case 1: return Calendar.TUESDAY;
            case 2: return Calendar.WEDNESDAY;
            case 3: return Calendar.THURSDAY;
            case 4: return Calendar.FRIDAY;
            case 5: return Calendar.SATURDAY;
            case 6: return Calendar.SUNDAY;
            default: return Calendar.MONDAY;
        }
    }

    private String getWeekDay(int i) {
        switch(i) {
            case 2: return "Montag";
            case 3: return "Dienstag";
            case 4: return "Mittwoch";
            case 5: return "Donnerstag";
            case 6: return "Freitag";
            case 7: return "Samstag";
            case 1: return "Sonntag";
            default: return "NaN";
        }
    }
}
