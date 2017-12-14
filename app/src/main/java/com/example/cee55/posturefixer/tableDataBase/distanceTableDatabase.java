
/**
 * Created by ced on 14/12/17.
 */

package com.example.cee55.posturefixer.tableDataBase;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "distTable")
public class distanceTableDatabase {
    //TODO Succed to get Number to double
    //private String dateTime; //Key Payload
    private String dateTime; //Key Payload
    private String bottom;
    private String top;
    private String userId;

    //public String getDateTime() {
    @DynamoDBHashKey(attributeName = "dateTime")
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;//Integer.parseInt(dateTime);
    }

    @DynamoDBAttribute(attributeName = "Bottom")
    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    @DynamoDBAttribute(attributeName = "Top")
    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    @DynamoDBAttribute(attributeName = "userID")
    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString(){
        return "[DateTime : " + dateTime + " userID " + userId + " Bottom " + bottom +  " top " + top + "]";
    }
}
