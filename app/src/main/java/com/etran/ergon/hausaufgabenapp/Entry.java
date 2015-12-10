package com.etran.ergon.hausaufgabenapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// to add the gson.jar file
// you have to right-click it and
// choose "add as library"
import com.google.gson.Gson;

/**
 * Created by etran on 11/20/15.
 */
public class Entry {
    private String subject;
    private String description;
    private Date duedate;
    private boolean toDelete;

    // save this object with sharedpreferences
    Entry(String subject, String description, Date duedate) {
        this.subject = subject;
        this.description = description;
        this.duedate = duedate;
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

    public String getDuedateS() {
        String myFormat = "EEE. dd. MMM. yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        return sdf.format(duedate);
    }
    public Date getDuedate() {
        return duedate;
    }
    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public boolean toDelete() {
        return toDelete;
    }
    public void setToDelete(boolean b) {
        toDelete = b;
    }

    public String getKey() {
        return subject + "/" + description + "/" + duedate.toString();
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
