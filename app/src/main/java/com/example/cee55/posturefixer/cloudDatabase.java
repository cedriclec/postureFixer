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

import com.example.cee55.posturefixer.tableDataBase.distanceTable;
import com.example.cee55.posturefixer.tableDataBase.distanceTableDatabase;


/**
 * Synchronous implementation for the DynamoDB table access using the Document API.  It is expected
 * that this object is wrapped into AsyncTasks for an android app.
 */

public class cloudDatabase extends AppCompatActivity {
    private final String DYNAMODB_TABLE = "distanceTable";
    private Context context;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonDynamoDBClient dbClient;

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
                    res[0] = new distanceTable(selectedDistance.get(0));
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
        Log.d("Cloud Database", "Result got " + res[0].toString());
        return res[0];
    }


    public static distanceTable getOneSpecificRow(final String hashKey, Context context) {
        final DynamoDBMapper mapper = getMapperForRequest(context);
        Log.d("Cloud Database", "Get row from this hashKey " + hashKey);
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
        Log.d("Cloud Database", "Result got " + res.toString());
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