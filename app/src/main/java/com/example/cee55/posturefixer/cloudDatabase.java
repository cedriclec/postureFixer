package com.example.cee55.posturefixer;

/**
 * Created by ced on 13/12/17.
 */

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobileconnectors.dynamodbv2 .document.UpdateItemOperationConfig;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
/*import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;*/

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.cee55.posturefixer.tableDataBase.distanceTable;
import com.example.cee55.posturefixer.tableDataBase.distanceTableDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * Synchronous implementation for the DynamoDB table access using the Document API.  It is expected
 * that this object is wrapped into AsyncTasks for an android app.
 */

public class cloudDatabase extends AppCompatActivity {
    //TODO May be handle date directly
    public static distanceTable[] getRowsFromDateInterval(Context context, final String dateBegin, final String dateEnd) {
        Log.d("Cloud Database", "Get rows from " + dateBegin + " to " + dateEnd);

        final DynamoDBMapper mapper = getMapperForRequest(context);
        final distanceTable[] resTmp = new distanceTable[200]; //Create array to return value from thread
        final int[] nbRow = new int[1];
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Map<String, AttributeValue> exprAttrValue = new HashMap<String, AttributeValue>();
                    exprAttrValue.put(":val1", new AttributeValue().withS(dateBegin));
                    exprAttrValue.put(":val2", new AttributeValue().withS(dateEnd));
                    Map<String, String> exprAttrName = new HashMap<String, String>();
                    exprAttrName.put("#c", "dateTime");
                    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                            .withFilterExpression("#c <= :val1 and #c >= :val2").withExpressionAttributeValues(exprAttrValue).withExpressionAttributeNames(exprAttrName);
                    final PaginatedScanList<distanceTableDatabase> selectedDistance = (mapper.scan(distanceTableDatabase.class, scanExpression));
                    getEveryRowFromScanList(selectedDistance, resTmp, nbRow);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        distanceTable[] res = new distanceTable[nbRow[0]];
        for (int i = 0; i < res.length; ++i){
            res[i] = resTmp[i];
        }
        Log.d("Cloud Database", "End of query getRowsFromDateInterval " + res.length);
        for (int i = 0; i < res.length; ++i){
            Log.d("Cloud Database", "Result getRowsFromDateInterval res[" + i + "]=" + res[i].toString());
        }
        return res;
    }

    private static void getEveryRowFromScanList(PaginatedScanList<distanceTableDatabase> resScan, distanceTable[] res, int[] nbRow){
        int i = 0;
        nbRow[0] = resScan.size();
        Log.d("getEveryRowFromScanList", "getEveryRowFromScanList " + nbRow[0] );
        while (i < nbRow[0]){
            res[i] = new distanceTable(resScan.get(i));
            ++i;
        }
    }

    //TODO Find a better query for mostrecentOne
    public static distanceTable getLastItemInserted(Context context) {
        Log.w("Cloud Database", "Last item inserted do not mean most recent one");
        final DynamoDBMapper mapper = getMapperForRequest(context);
        Log.d("Cloud Database", "Get last item");
        final distanceTable[] res = {new distanceTable()}; //Create array to return value from thread
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                    final PaginatedScanList<distanceTableDatabase> selectedDistance = (mapper.scan(distanceTableDatabase.class, scanExpression));
                    final int mostRecentIndice = getMostRecentRow(selectedDistance);
                    res[0] = new distanceTable(selectedDistance.get(mostRecentIndice));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Cloud Database", "Result getLastItemInserted " + res[0].toString());
        return res[0];
    }

    private static int getMostRecentRow(PaginatedScanList<distanceTableDatabase> res){
        int indiceMostRecent = 0;
        double mostRecentDate = Double.parseDouble(res.get(0).getDateTime()); //TODO Imrpove this by passing through distance table without new
        int i = 0;
        int sizeList = res.size();
        for(i = 0; i < sizeList; ++i ){
            double currentDate = Double.parseDouble(res.get(i).getDateTime());
            if (currentDate >= mostRecentDate){
                mostRecentDate = currentDate;
                indiceMostRecent = i;
            }
        }
        return indiceMostRecent;
    }

    public static distanceTable getOneSpecificRow(final String hashKey, Context context) {
        final DynamoDBMapper mapper = getMapperForRequest(context);
        Log.d("Cloud Database ", "Get row from this hashKey " + hashKey);
        final distanceTableDatabase[] selectedDistance = {new distanceTableDatabase()}; //Create array to return value from thread
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    selectedDistance[0] = mapper.load(distanceTableDatabase.class, hashKey);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        distanceTable res = new distanceTable(selectedDistance[0]);
        Log.d("Cloud Database", "Result getOneSpecificRow " + res.toString());
        return res;
    }

    private static DynamoDBMapper getMapperForRequest(Context context){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "051896657986",
                "us-west-2:d1aa16a3-8b53-4ea5-a8c8-e34389142bd2", // Identity pool ID
                "arn:aws:iam::051896657986:role/Cognito_postureFixerAppUnauth_Role",
                null,
                Regions.US_WEST_2 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
        return new DynamoDBMapper(ddbClient);
    }


    //Don't work
    //TODO Try with scan
    /*
    //DOES NOT WORK !!!!!!!!!!!!!
    public static distanceTable getLastItem(Context context) {
        final DynamoDBMapper mapper = getMapperForRequest(context);
        Log.d("Cloud Database", "Get last item");
        final distanceTable[] res = {new distanceTable()}; //Create array to return value from thread
        //final PaginatedQueryList<distanceTableDatabase> selectedDistance = {new  PaginatedQueryList<distanceTableDatabase>()};
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                            .withHashKeyValues("*")
                            .withScanIndexForward(true)
                            .withLimit(1);
                    final PaginatedQueryList<distanceTableDatabase> selectedDistance = (mapper.query(distanceTableDatabase.class, queryExpression));
                    res[0] = new distanceTable(selectedDistance.get(0));
                    Log.d("Cloud Database", "Result got " + selectedDistance.toString());

                    //res[0] = new distanceTable(selectedDistance.get(0));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        //distanceTable res = new distanceTable(selectedDistance[0].get(0));
        Log.d("Cloud Database", "Result got " + res[0].toString());
        return res[0];
    }*/
}