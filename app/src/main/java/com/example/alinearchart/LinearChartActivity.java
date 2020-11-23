package com.example.alinearchart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Random;
public class LinearChartActivity extends Activity {
    private XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    private XYMultipleSeriesRenderer renderer2 = new XYMultipleSeriesRenderer();

    private XYSeries currentSeries;
    private XYSeriesRenderer currentRenderer;
    private String dateFormat;
    private GraphicalView chartView;
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        dataset = (XYMultipleSeriesDataset) savedInstanceState
                .getSerializable("dataset");
        renderer = (XYMultipleSeriesRenderer) savedInstanceState
                .getSerializable("renderer");
        //幹幹幹幹幹幹
        //renderer.setBackgroundColor(Color.RED);
        //renderer.setApplyBackgroundColor(true);
        //幹幹幹幹幹幹
        currentSeries = (XYSeries) savedInstanceState
                .getSerializable("current_series");
        currentRenderer = (XYSeriesRenderer) savedInstanceState
                .getSerializable("current_renderer");
        dateFormat = savedInstanceState.getString("date_format");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
// TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putSerializable("dataset", dataset);
        outState.putSerializable("renderer", renderer);
        outState.putSerializable("current_series", currentSeries);
        outState.putSerializable("current_renderer", currentRenderer);
        outState.putString("date_format", dateFormat);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_chart);
        String seriesTitle = "上證指數";
        XYSeries series = new XYSeries(seriesTitle);
        dataset.addSeries(series);
        currentSeries = series;
        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();

        //seriesRenderer().setApplyBackgroundColor(true);
        seriesRenderer.setColor(Color.BLUE);
        seriesRenderer.setFillBelowLine(false);//折線下面區域是否新增顏色
        seriesRenderer.setFillBelowLineColor(Color.GRAY);//折線下面區域顏色顏色的設置
        seriesRenderer.setPointStyle(PointStyle.POINT);
        seriesRenderer.setLineWidth(100);
        renderer.addSeriesRenderer(seriesRenderer);

////   新增另外一條曲線圖
//        XYSeriesRenderer seriesRenderer2 = new XYSeriesRenderer();
//   seriesRenderer2 = new XYSeriesRenderer();
//   seriesRenderer2.setColor(Color.GREEN);
//   seriesRenderer2.setFillBelowLine(true);
//   seriesRenderer2.setFillBelowLineColor(Color.YELLOW);
//   seriesRenderer2.setPointStyle(PointStyle.POINT);
//   seriesRenderer2.setLineWidth(3);
//   renderer2.addSeriesRenderer(seriesRenderer);
////   新增另外一條曲線圖
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(100);
        renderer.setXAxisMax(20);
        renderer.setShowGrid(false);
        renderer.setXLabels(20);
        renderer.setChartTitle("上證指數-24小時趨勢圖");
        currentRenderer = seriesRenderer;
        //用xyValues設定劃出圖
        double x = 0;
        double y = 0;
        int[][] xyValues = getValues();
  // for (int i = 0; i < 2; i++  ) {

        for (int j = 0; j < xyValues.length; j++  ) {
            x=xyValues[j][0];
            y=xyValues[j][1];
            currentSeries.add(x, y);
        }
   //}
    }
    @SuppressLint("ResourceType")
    @Override
    protected void onResume() {
// TODO Auto-generated method stub
        if(chartView == null){
            LinearLayout layout=(LinearLayout) findViewById(R.id.chart);
            chartView=ChartFactory.getLineChartView(this, dataset, renderer);
            layout.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }else {
            chartView.setBackgroundResource(R.id.chart);
            chartView.repaint();
        }
        super.onResume();
    }
    private int[][] getValues() {
// TODO Auto-generated method stub
        int[][] xyValues = new int[100][2];//折線圖X值與幾條
        Random rand=new Random();
        for (int i = 0; i < xyValues.length; i++  ) {
            xyValues[i][0]=i;
            xyValues[i][1]=rand.nextInt(100);  //Y值上限
        }
        return xyValues;
    }
}