package com.example.weighttracker;

import java.util.Date;

public class Entry {
    private double weight;
    private long date;
    private int id;

    public Entry (int i, long d, double w) {
        id = i;
        weight = w;
        date = d;
    }

    public void setWeight(double d) { this.weight = d; }

    public double getWeight() {
        return weight;
    }

    public void setDate(long s) { this.date = s; }

    public long getDate() { return date; }

    public int getId() { return id; }
}
