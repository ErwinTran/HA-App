package com.etran.ergon.hausaufgabenapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<Entry> {

    private ArrayList<Entry> entries;

    public MyArrayAdapter(Context context, ArrayList<Entry> entries) {
        super(context, 0, entries);
        this.entries = entries;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Entry entry = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_entry, parent, false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListActivity)getContext()).clickItem(entry);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((ListActivity)getContext()).longClickItem(entry);
                return true;
            }
        });

        TextView title = (TextView) convertView.findViewById(R.id.titel);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        ToggleButton tButton = (ToggleButton) convertView.findViewById(R.id.tButton);

        title.setText(entry.getSubject());
        date.setText(entry.getFormattedDate());

        tButton.setText("DONE");
        tButton.setTextOff("DONE");
        tButton.setTextOn("cancel");
        tButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                entry.setToDelete(isChecked);
            }
        });

        return convertView;
    }

    public ArrayList<Entry> getAllItems() {
        return entries;
    }

}
