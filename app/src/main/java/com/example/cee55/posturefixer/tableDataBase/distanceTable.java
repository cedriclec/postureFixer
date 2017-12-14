package com.example.cee55.posturefixer.tableDataBase;

/**
 * Created by ced on 13/12/17.
 */

public class distanceTable {
    private double dateTime; //Key Payload
    private double bottom;
    private double top;
    private double userId;

    public distanceTable() {
        dateTime = 0;
        bottom = 0;
        top = 0;
        userId = 0;
    }
    public distanceTable(distanceTableDatabase distDB){
        dateTime = Double.parseDouble(distDB.getDateTime());
        bottom = Double.parseDouble(distDB.getBottom());
        top = Double.parseDouble(distDB.getTop());
        userId = Double.parseDouble(distDB.getuserId());
    }

    public String toString(){
        return "[DateTime : " + dateTime + " userID " + userId + " Bottom " + bottom +  " top " + top + "]";
    }

    public double getBottom(){
        return bottom;
    }

    public double getTop(){
        return top;
    }

    public double getDatetime(){
        return dateTime;
    }
}