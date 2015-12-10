package com.etran.ergon.hausaufgabenapp;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Setting {

    private ArrayList<String> classes;
    private int day1;
    private int day2;

    Setting() {
        classes = new ArrayList<>();
        day1 = 0;
        day2 = 1;
    }
    Setting(ArrayList<String> classes, int day1, int day2) {
        this.classes = classes;
        this.day1 = day1;
        this.day2 = day2;
    }

    public int getDay2() {
        return day2;
    }

    public void setDay2(int day2) {
        this.day2 = day2;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public void getClasses(ArrayList<String> subjects) {
        this.classes = subjects;
    }

    public int getDay1() {
        return day1;
    }

    public void setDay1(int day1) {
        this.day1 = day1;
    }

    // Shared Preferences stuff
    public String serialize() {
        // Class -> Json
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public Setting create(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, Setting.class);
    }
}
