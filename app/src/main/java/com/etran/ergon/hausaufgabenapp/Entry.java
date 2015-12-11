package com.etran.ergon.hausaufgabenapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// to add the gson.jar file
// you have to right-click it and
// choose "add as library"
import com.google.gson.Gson;

public class Entry {
    private String subject;
    private String description;
    private Date date;
    private boolean toDelete;

    // save this object with sharedpreferences
    Entry(String subject, String description, Date date) {
        this.subject = subject;
        this.description = description;
        this.date = date;
        toDelete = false;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormattedDate() {
        String myFormat = "EEE. dd. MMM. yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        return sdf.format(date);
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public boolean toDelete() {
        return toDelete;
    }
    public void setToDelete(boolean b) {
        toDelete = b;
    }

    public String getKey() {
        return subject + "/" + description + "/" + date.toString();
    }

    // Shared Preferences stuff
    public String serialize() {
        // Class -> Json
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public Entry create(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, Entry.class);
    }
}
