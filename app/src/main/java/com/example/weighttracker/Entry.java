package com.example.weighttracker;

public class Entry {
    private double weight;
    private String date = "";
    private int id;

    public Entry (int i, String d, double w) {
        id = i;
        weight = w;
        date = d;
    }

    public void setWeight(double d) { this.weight = d; }

    public double getWeight() {
        return weight;
    }

    public void setDate(String s) { this.date = s; }

    public String getDate() { return date; }

    public int getId() { return id; }
}
