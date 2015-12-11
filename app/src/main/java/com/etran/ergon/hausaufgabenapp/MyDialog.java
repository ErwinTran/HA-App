package com.etran.ergon.hausaufgabenapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class MyDialog extends Dialog {

    private Activity a;
    private ArrayAdapter<String> subjectsAdapter;
    private EditText inputSubject;
    private Button btCancel;
    private Button btAdd;

    public MyDialog(Activity activity, ArrayAdapter<String> subjectsAdapter) {
        super(activity);
        this.a = activity;
        this.subjectsAdapter = subjectsAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_new);
        setTitle("Neues Fach:");

        inputSubject = (EditText) findViewById(R.id.inputSubject);
        inputSubject.setBackgroundResource(R.drawable.edittext_bg);
        // opens the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btCancel = (Button) findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btAdd = (Button) findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectsAdapter.add(inputSubject.getText().toString());
                dismiss();
            }
        });
    }
}
