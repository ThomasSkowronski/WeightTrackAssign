package com.example.weighttracker;

public class Entry {
    private double kg;
    private double lbs;
    private long date;
    private int id;

    public Entry (int i, long d, double k,double lb) {
        id = i;
        kg = k;
        lbs = lb;
        date = d;
    }

    public double getKg () { return kg; }

    public void setKg(double kg) { this.kg = kg; }

    public double getLbs() { return lbs; }

    public void setLbs(double lbs) { this.lbs = lbs; }

    public void setDate(long s) { this.date = s; }

    public long getDate() { return date; }

    public int getId() { return id; }
}
