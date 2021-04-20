package com.example.weighttracker;

public class User {
    private String name = "username";
    private String units = "lbs";
    private double weight = 0;

    public void setWeight(double d){
        weight = d;
    }

    public double getWeight() {
        return weight;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }

    public void setUnits(String s) {
        this.units = s;
    }

    public String getUnit() {
        return units;
    }

    public static final User mainuser = new User();

    public static User getInstance() {
        return mainuser;
    }

}