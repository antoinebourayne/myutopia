package com.example.myutopia.classes;

public class DayInfo {

    //attributes

    private float  minTemp;
    private float  maxTemp;
    private String dayName;

    //constructors

    public DayInfo(float minTemp, float maxTemp, String dayName) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.dayName = dayName;
    }

    //getters

    public float  getMinTemp() {
        return minTemp;
    }
    public float  getMaxTemp() {
        return maxTemp;
    }
    public String getDayName() {
        return dayName;
    }


    //setters

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }
    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }
    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

}
