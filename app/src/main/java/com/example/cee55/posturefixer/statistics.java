package com.example.cee55.posturefixer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cee55.posturefixer.tableDataBase.distanceTable;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class statistics extends AppCompatActivity {
    private GraphicalView mChartView;
    distanceTable res3[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));

        // TEST
        Context context = getApplicationContext();
       // distanceTable res2 = cloudDatabase.getLastItemInserted(context);
         res3 = cloudDatabase.getRowsFromDateInterval(context,"20171215175519", "20171215200000");
        //if(null!=getIntent().getStringExtra("startTime"))
          //    res3 = cloudDatabase.getRowsFromDateInterval(context,getIntent().getStringExtra("startTime"), getIntent().getStringExtra("finishTime"));

        // ENDTEST

        if(res3.length!=0 && getIntent().getStringExtra("distanceOne")!= null)
            drawChart();
        else
            Toast.makeText(getApplicationContext(),  "null", Toast.LENGTH_SHORT).show();

    }

    public void showExercise(View view){

        Intent intent_01 = new Intent(getApplicationContext(), exercise.class);
        intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
        intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
        startActivity(intent_01);
    }

    private void drawChart() {
        String[] time = new String[res3.length];
        int[] bottom = new int[res3.length];
        int[] top = new int[res3.length];
        int[] x = new int[res3.length];
        int[] result = new int[res3.length];

            double distanceOne = Double.parseDouble(getIntent().getStringExtra("distanceOne"));
            double distanceTwo = Double.parseDouble(getIntent().getStringExtra("distanceTwo"));
            double inclination;
            for (int i = 0; i < res3.length; i++) {
                time[i] = Integer.toString((int) res3[i].getDatetime());
                bottom[i] = (int) res3[i].getBottom();
                top[i] = (int) res3[i].getTop();
                x[i] = i + 1;

               inclination = (distanceOne + distanceTwo) / (top[i] + bottom[i]);
                if (!(inclination < 0.93 && inclination > 0.65)) {
                    result[i] = 1; //bad posture
                } else
                    result[i] = 0; //good posture*/


            }

        XYSeries resultSeries = new XYSeries("1 : Good, 0 : Bad");

// Adding data to Income and Expense Series
        for(int i=0;i<x.length;i++){
            resultSeries.add(x[i], result[i]);
        }

// Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();


        dataset.addSeries(resultSeries);


       XYSeriesRenderer resultRenderer = new XYSeriesRenderer();
        resultRenderer.setColor(Color.DKGRAY);
        resultRenderer.setPointStyle(PointStyle.CIRCLE);
        resultRenderer.setFillPoints(true);
        resultRenderer.setLineWidth(2);
        resultRenderer.setDisplayChartValues(true);

// Creating a XYMultipleSeriesRenderer to customize the whole chart

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setGridLineWidth(10);
        multiRenderer.setYAxisMax(1.5);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setXAxisMin(0.97);
        multiRenderer.setXAxisMax(time.length+0.03);
        multiRenderer.setXLabelsPadding(10);
        multiRenderer.setAxisTitleTextSize(25);
        multiRenderer.setChartTitleTextSize(30);
        multiRenderer.setScale(50);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setXLabels(0);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setChartTitle("Your Posture");
        multiRenderer.setXTitle("Time");
        multiRenderer.setYTitle("Distance");
        multiRenderer.setZoomButtonsVisible(true);
        for(int i=0;i<x.length;i++){
            multiRenderer.addXTextLabel(i+1, time[i]);
        }

        multiRenderer.addSeriesRenderer(resultRenderer);


        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart_line);
          mChartView = ChartFactory.getLineChartView(this, dataset, multiRenderer);
            multiRenderer.setClickEnabled(true);
            multiRenderer.setSelectableBuffer(10);
            multiRenderer.setSelectableBuffer(10);
            multiRenderer.setSelectableBuffer(10);
            layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

        } else {
            mChartView.repaint();
        }
    }
}
