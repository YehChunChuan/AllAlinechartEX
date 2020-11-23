package com.example.alinearchart;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

//折線圖 類
public class LineChart
{
    // 渲染資料集 （圖表的資料，折線的折點座標資訊等）
    private XYMultipleSeriesDataset mDataset;

    // 多重圖層渲染器 （可以看做是總渲染器(含多個子渲染器)，繪製背景網格，折線圖等）
    private XYMultipleSeriesRenderer mMltRenderer;

    // 折線集合
    private List<XYSeries> mSeriesList;


    // 標記折線圖是否可以拖動
    public boolean mIsCanMove = true;

    // 圖表檢視（就是最後繪製出來的整個折線圖）
    public GraphicalView mChartView;

    //無參建構函式 (如果有 有引數的建構函式，就不會自動新增無參建構函式，最好自己加上無參建構函式，)
    public LineChart(){}


    /* 有參建構函式
     *   設定圖表屬性
     *   xTitle：X軸標題
     *   yTitle：Y軸標題
     *   xMin：X軸的最小值
     *   xMax：X軸的最大值
     *   yMin：Y軸的最小值
     *   yMax：Y軸的最大值
     *   axisColor：座標軸的顏色
     *   labelsColor：標籤的顏色（標籤：也就是座標軸上的刻度值10，20...80）
     */
    public LineChart(String xTitle, String yTitle, double xMin,
                     double xMax, double yMin, double yMax, int axisColor, int labelsColor)
    {
        mDataset = new XYMultipleSeriesDataset();// 例項化資料集物件
        mMltRenderer = new XYMultipleSeriesRenderer();// 例項化多層渲染器物件
        mSeriesList = new ArrayList<XYSeries>(); // 初始化折線集合

        mMltRenderer.setXTitle(xTitle);// 設定X軸標題
        mMltRenderer.setYTitle(yTitle);// 設定Y軸標題
        mMltRenderer.setXAxisMin(xMin);// 設定X軸的最小值
        mMltRenderer.setXAxisMax(xMax);// 設定X軸的最大值
        mMltRenderer.setYAxisMin(yMin);// 設定Y軸的最小值
        mMltRenderer.setYAxisMax(yMax);// 設定Y軸的最大值
        mMltRenderer.setAxesColor(axisColor);// 設定座標軸顏色
        mMltRenderer.setLabelsColor(labelsColor);// 設定標籤(刻度值）顏色
        mMltRenderer.setShowGrid(true);// 顯示網格
        mMltRenderer.setGridColor(Color.GRAY);// 設定網格顏色
        mMltRenderer.setXLabels(10);// 設定X軸的標籤數（有幾個刻度）
        mMltRenderer.setXLabelsPadding(10);//設定X軸標籤的間距
        mMltRenderer.setYLabels(16);// 設定Y軸的標籤數
        mMltRenderer.setYLabelsAlign(Align.RIGHT);// 設定Y軸標籤的方向
        mMltRenderer.setPointSize((float) 2);//設定折線點的大小
        mMltRenderer.setShowLegend(true);// 下面的 圖例標註，如圓點的藍色的折線是X軸...
        mMltRenderer.setZoomButtonsVisible(false);// 隱藏放大縮小按鈕
        mMltRenderer.setZoomEnabled(true, false);// 設定縮放,這邊是橫向可以縮放,豎向不能縮放
        mMltRenderer.setPanEnabled(true, false);// 設定滑動,這邊是橫向可以滑動,豎向不可滑動

    }

    //新增一條折線到圖表
    public void addLineToChart(String lineTitle, PointStyle pointStyle, int lineColor)
    {
        XYSeriesRenderer serRender = new XYSeriesRenderer();//建立1個子渲染器
        XYSeries series = new XYSeries(lineTitle);//建立1條折線

        mMltRenderer.addSeriesRenderer(serRender);// 將子渲染器新增到總渲染器
        mDataset.addSeries(series);// 將折線新增到資料集
        mSeriesList.add(series);// 將折線新增到折線集合

        // 設定折線渲染屬性
        serRender.setPointStyle(pointStyle);// 設定折點的樣式
        serRender.setColor(lineColor);// 設定折線的顏色
        serRender.setFillPoints(true);// 設定折點是實心還是空心
        serRender.setDisplayChartValues(false);// 不顯示折點的Y值
        serRender.setDisplayChartValuesDistance(10);// 設定數值與折點的距離

    }

    //設定圖表的顯示頁面 (activity：圖表顯示所在的頁面)
    public void setChartViewActivity(final Activity activity)
    {
        if (mChartView == null)
        {
            //獲取一個layout,用來顯示圖表
            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.chart);
            //生成圖表
            mChartView = ChartFactory.getLineChartView(activity, mDataset,mMltRenderer);

            mMltRenderer.setClickEnabled(true);// 可以響應點選
            mMltRenderer.setSelectableBuffer(10);// 設定點的緩衝半徑值(在某點附近點選時,在點的半徑範圍內都算點選這個點)

            //折線的點選響應
            mChartView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 拿到點選的折線物件、折點
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    if (seriesSelection != null)
                    {
                        // 將所點選的點的資訊通過Toast展示(點選的是那一條折線，第幾個折點，座標值)
                        Toast.makeText(activity,
                                "折線:"+ seriesSelection.getSeriesIndex()
                                        + "\n點: "+ seriesSelection.getPointIndex()
                                        + "\nX="+ seriesSelection.getXValue() + ", Y="+ seriesSelection.getValue(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 將圖表新增到layout中
            layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
        else
        {
            mChartView.repaint();//繪製折線圖(重繪，更新)
        }
    }

    // 向某條折線新增折點(x,y) （新增資料）lineTitle：折線的標題
    public void addData(String lineTitle, double x, double y)
    {
        if(mSeriesList.size() > 0 )//有折線
        {
            for(XYSeries ser : mSeriesList)//遍歷折線集合
            {
                if(ser.getTitle().equals(lineTitle))// 找到指定的折線
                {
                    ser.add(x, y);
                    break;
                }
            }
        }
    }

    // 拖動圖表  設定X軸的當前顯示向右移val
    public void moveChart(int val)
    {
        // 計算當前X軸可視長度，也就是當前看到的X軸上的右刻度與左刻度之差(縮放按鈕能夠影響看到的刻度值範圍)
        double dis = mMltRenderer.getXAxisMax() - mMltRenderer.getXAxisMin();

        double max = val + mMltRenderer.getXAxisMax();
        double min = max - dis;
        mMltRenderer.setXAxisMin(min);// 設定X軸顯示的左刻度值
        mMltRenderer.setXAxisMax(max);// 設定X軸顯示的右刻度值

    }
}