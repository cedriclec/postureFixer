package com.example.cee55.posturefixer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

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
  String str =
            "[ { 'Bottom1' : 20, 'Bottom2' : 0, 'dateTime' : '19:35', 'Top1' : 10.984, 'Top2' : 0, 'userID' : 1},"+
                    "{ 'Bottom1' : 10, 'Bottom2' : 0, 'dateTime' : '19:40', 'Top1' : 215.354, 'Top2' : 0, 'userID' : 1}]";
    JSONArray jarray = new JSONArray(str);
    private String[] time = new String[jarray.length()];
    private int[] bottom = new int[jarray.length()];
    private int[] top = new int[jarray.length()];
    private int[] x = new int[jarray.length()];
    private GraphicalView mChartView;

    public statistics() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));

        // TEST
        Context context = getApplicationContext();
        distanceTable res = cloudDatabase.getOneSpecificRow("20171214", context);
        distanceTable res2 = cloudDatabase.getLastItemInserted(context);
        distanceTable res3[] = cloudDatabase.getRowsFromDateInterval(context,"20171215144106", "20171215101010");
        // ENDTEST

        try {
            drawChart();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showExercise(View view){

        Intent intent_01 = new Intent(getApplicationContext(), exercise.class);
        intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
        intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
        startActivity(intent_01);
    }

    private void drawChart() throws JSONException {
        for(int i=0; i < jarray.length(); i++){
            JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
            time[i] = jObject.getString("dateTime");
            bottom[i] = jObject.getInt("Bottom1");
            top[i] = jObject.getInt("Top1");
            x[i] = i+1;
        }


// Creating an  XYSeries for Income
        XYSeries bottomSeries = new XYSeries("Bottom");

// Creating an  XYSeries for Expense
        XYSeries topSeries = new XYSeries("Top");

// Adding data to Income and Expense Series
        for(int i=0;i<x.length;i++){
            bottomSeries.add(x[i], bottom[i]);
            topSeries.add(x[i],top[i]);
        }

// Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

// Adding bottom Series to the dataset
        dataset.addSeries(bottomSeries);

// Adding Expense Series to dataset
        dataset.addSeries(topSeries);

// Creating XYSeriesRenderer to customize bottomSeries
        XYSeriesRenderer bottomRenderer = new XYSeriesRenderer();
        bottomRenderer.setColor(Color.DKGRAY);
        bottomRenderer.setPointStyle(PointStyle.CIRCLE);
        bottomRenderer.setFillPoints(true);
        bottomRenderer.setLineWidth(2);
        bottomRenderer.setDisplayChartValues(true);

// Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer topRenderer = new XYSeriesRenderer();
        topRenderer.setColor(Color.RED);
        topRenderer.setPointStyle(PointStyle.CIRCLE);
        topRenderer.setFillPoints(true);
        topRenderer.setLineWidth(2);
        topRenderer.setDisplayChartValues(true);

// Creating a XYMultipleSeriesRenderer to customize the whole chart
        int[] temp = new int[bottom.length + top.length];
        System.arraycopy(bottom, 0, temp, 0, bottom.length);
        System.arraycopy(top, 0, temp, 0, top.length);
        int max = 0;
        for (int index = 0; index < temp.length; index++){
            if(max < temp[index])
                max = temp[index];
        }
        max = max + 10;

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setGridLineWidth(10);
        multiRenderer.setYAxisMax(max);
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

// Adding bottomRenderer and topRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(bottomRenderer);
        multiRenderer.addSeriesRenderer(topRenderer);

// Creating an intent to plot line chart using dataset and multipleRenderer
        // Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataset, multiRenderer);


        // Start Activity
        //startActivity(intent);

        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart_line);
           /* ViewGroup.LayoutParams params = layout.getLayoutParams();
            params.height = 100;
            params.width = 100;
            layout.setLayoutParams(params);*/
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
