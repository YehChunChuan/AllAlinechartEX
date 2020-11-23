package com.example.alinearchart;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Random;
public class ALinearChart {
    private final int SERIES_NR=3;
    private final int NR=20;
    public Intent execute(Context context) {
        return ChartFactory.getLineChartIntent(context, getLinearChartDataset(), getLinearChartRenderer(), "Linear Chart Another");
    }
    private XYMultipleSeriesRenderer getLinearChartRenderer() {
// TODO Auto-generated method stub
        int[] linearColor={Color.RED, Color.GREEN, Color.BLUE};
        int[] belowLinearColor={Color.YELLOW, Color.CYAN, Color.GRAY};
        XYMultipleSeriesRenderer renderer=new XYMultipleSeriesRenderer();
        XYSeriesRenderer tempRender;
        for (int i = 0; i < SERIES_NR; i++  ) {
            tempRender=new XYSeriesRenderer();
            tempRender.setColor(linearColor[i]);
//     tempRender.setFillBelowLine(true);
//     tempRender.setFillBelowLineColor(belowLinearColor[i]);
            tempRender.setPointStyle(PointStyle.POINT);
            tempRender.setLineWidth(2);
            renderer.addSeriesRenderer(tempRender);
        }
        setLinearChartRenderer(renderer);
        return renderer;
    }
    private void setLinearChartRenderer(XYMultipleSeriesRenderer renderer) {
// TODO Auto-generated method stub
        renderer.setChartTitle("Three Linear Chart");
        renderer.setXAxisMax(20);
        renderer.setXAxisMin(0);
        renderer.setYAxisMax(60);
        renderer.setYAxisMin(0);
    }
    private XYMultipleSeriesDataset getLinearChartDataset() {
// TODO Auto-generated method stub
        XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
        for (int i = 0; i < SERIES_NR; i++  ) {
            XYSeries series=new XYSeries("Stock Trend---" ,i);
            int temp=10*i;
            int[][] data=getBasicData();
            for (int j = 0; j < data.length; j++  ) {
                int x=data[j][0];
                int y=temp=data[j][1];
                series.add(x, y);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }
    private int[][] getBasicData() {
        int[][] data=new int[20][2];
        Random rand=new Random();
        for (int i = 0; i < data.length; i++  ) {
            data[i][0]=i;
            data[i][1]=rand.nextInt(20);
        }
        return data;
    }
}