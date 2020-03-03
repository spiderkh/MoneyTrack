package com.bhaskar.moneytrack;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class PieChartClass extends AppCompatActivity {
    private static  String TAG="PieChartClass";

    private float[] yData={10.f,20.7f,10f,30f};
    private String[] xData={"a","b","c","d"};
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_class);

        pieChart=findViewById(R.id.piechartid);
        pieChart.setHoleRadius(10f);

        addDataSet(pieChart);
    }

    private void addDataSet(PieChart pieChart) {

        Log.d(TAG, "addDataSet: ");
        ArrayList<PieEntry> yEntry=new ArrayList<>();
        ArrayList<String> xEntry=new ArrayList<>();

        for(int i=0;i<yData.length;i++)
        {
          yEntry.add(new PieEntry(yData[i],xData[i]));
        }

        for (int i=0;i<xData.length;i++)
        {
            xEntry.add(xData[i]);
        }
        PieDataSet pieDataSet=new PieDataSet(yEntry,"employee List");
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueTextSize(12);



        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);
        colors.add(Color.YELLOW);
        pieDataSet.setColors(colors);

        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
       // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
