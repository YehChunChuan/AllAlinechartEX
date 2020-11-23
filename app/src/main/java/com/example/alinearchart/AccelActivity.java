package com.example.alinearchart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
//import com.situ.model.Accelerate_info;

//試試看Push到GitHub


public class AccelActivity extends Activity{
    public SensorManager sensorManager;
    public Sensor accelSensor ;
    TextView xText ;
    TextView yText ;
    TextView zText ;
    TextView sumText;
    TextView danWei ;
    TextView title;
    private Vibrator vibrator;
    SensorEventListener threeParamListener;
    SensorEventListener oneParamListener;
    SensorEventListener twoParamListener;
    Handler avgHandler;
    Thread avgThread;
    int sensor_id = 0;
    //图表相关
    private XYSeries series;
    private XYMultipleSeriesDataset mDataset;
    private GraphicalView chart;
    private XYMultipleSeriesRenderer renderer;
    private Context context;
    private int yMax = 20;//y轴最大值，根据不同传感器变化
    private int xMax = 100;//一屏显示测量次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accel);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        avgHandler = new AveHandler();
        //给控件实例化
        if(xText==null){
            findViews();
        }
        Intent intent = getIntent();

        avgThread = new Thread(runnable);//定期更新平均值的线程启动
        avgThread.start();


        //初始化各个监听器
       initListeners();

        switch (1) {
            case Sensor.TYPE_ACCELEROMETER:
                title.setText("加速度传感器");
                danWei.setText("N/M^2");
                accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(threeParamListener, accelSensor, sensorManager.SENSOR_DELAY_UI);
                yMax = 20;
                sensor_id = 1;
                break;

            default:
                break;
        }

        //初始化图表
        //initChart("*0.5s", danWei.getText().toString(),0,xMax,0,yMax);
    }

    /**
     * 抓取view中文本控件的函数
     */
    private void findViews(){
        xText = (TextView) findViewById(R.id.xAxis);
        yText = (TextView) findViewById(R.id.yAxis);
        zText = (TextView) findViewById(R.id.zAxis);
        sumText = (TextView) findViewById(R.id.sum);
        danWei = (TextView) findViewById(R.id.danWei);
        title = (TextView) findViewById(R.id.realtime_title);
    }

    /**
     * 初始化各类监听器
     */
    private void initListeners() {


        threeParamListener = new SensorEventListener() {//有三个返回参数的监听器

            @Override
            public void onSensorChanged(SensorEvent event) {
                // TODO Auto-generated method stub
                xText.setText(event.values[0]+"");
                yText.setText(event.values[1]+"");
                zText.setText(event.values[2]+"");
                double sum = threeDimenToOne(event.values[0], event.values[1], event.values[2]);

                giveAverage(sum);//将当前测量的结果写入buffer，然后定期求buffer里面的平均值，并显示
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub
            }
        };

    }

    /**
     * 初始化图表
     */
    private void initChart(String xTitle,String yTitle,int minX,int maxX,int minY,int maxY){
        //这里获得main界面上的布局，下面会把图表画在这个布局里面
        LinearLayout layout = (LinearLayout)findViewById(R.id.realtime_chart);
        //这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线
        series = new XYSeries("历史曲线");
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
        //将点集添加到这个数据集中
        mDataset.addSeries(series);

        //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        int lineColor = Color.BLACK;
        PointStyle style = PointStyle.CIRCLE;
        renderer = buildRenderer(lineColor, style, true);

        //设置好图表的样式
        setChartSettings(renderer, xTitle,yTitle,
                minX, maxX, //x轴最小最大值
                minY, maxY, //y轴最小最大值
                Color.BLACK, //坐标轴颜色
                Color.WHITE//标签颜色
        );

        //生成图表
        chart = ChartFactory.getLineChartView(this, mDataset, renderer);

        //将图表添加到布局中去
        layout.addView(chart, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(threeParamListener!=null){
            sensorManager.unregisterListener(threeParamListener);
        }
        if(oneParamListener!=null){
            sensorManager.unregisterListener(oneParamListener);
        }
        if(avgThread!=null)
            avgThread.interrupt();


//        DbUtils db = DbUtils.create(getApplicationContext());//xUtils框架
//        try {
//            List<Accelerate_info> list = db.findAll(Selector.from(Accelerate_info.class).where("sensor", "=", sensor_id));//看看一共写入了数据库多少数据
//            System.out.println("数量是"+list.size());
//        } catch (DbException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


    }
    /**
     * 根据三个坐标向量求和向量的模
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static double threeDimenToOne(double x,double y,double z){
        return Math.sqrt(x*x+y*y+z*z);
    }
    public  int index = 0;//指示这段时间一共写入了多少个数据
    //在这里可以设置缓冲区的长度，用于求平均数
    double[] buffer = new double[500];//半秒钟最多放500个数
    public int INTERVAL = 500;//每半秒求一次平均值
    public double AVERAGE = 0;//存储平均值


    /**
     * 一个子线程，没隔固定时间计算这段时间的平均值，并给textView赋值
     */
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("线程已经启动");
            while(true){
                try {
                    Thread.sleep(INTERVAL);//没隔固定时间求平均数
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    avgThread = new Thread(runnable);
                    avgThread.start();
                }
                if(index!=0){
                    double sum = 0;
                    for (int i=0;i<index;i++) {
                        sum+=buffer[i];

                    }
                    AVERAGE = sum/new Double(index);
                    index=0;//让下标恢复
                }
                avgHandler.sendEmptyMessage(1);//提示handler更新ui

            }
        }
    };

    /**
     * 更新平均值的显示值
     */
    public void setAverageView(){//更新textView
        if(sumText==null)return;
        sumText.setText(AVERAGE+"");
    }
    /**
     * 每隔固定时间给平均值赋值，并且更新图表的操作
     * @author love fang
     *
     */
    class AveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            setAverageView();//显示平均值
            updateChart();//更新图表

            //

//            if(mSeriesList.size() > 0 )//有折線
//            {
//                for(XYSeries ser : mSeriesList)//遍歷折線集合
//                {
//                    if(ser.getTitle().equals(lineTitle))// 找到指定的折線
//                    {
//                        ser.add(x, y);
//                        break;
//                    }
//                }
//            }





//            把当前值存入数据库
//            DbUtils db = DbUtils.create(getApplicationContext());
//
//            Accelerate_info accelerate_info = new Accelerate_info(System.currentTimeMillis(), AVERAGE, sensor_id);
//            try {
//                db.save(accelerate_info);//将当前平均值存入数据库
//            } catch (DbException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                System.out.println("保存失败");
//            }
        }
    }
    /**
     * 接受当前传感器的测量值，存到缓存区中去，并将下标加一
     * @param data
     */
    public void giveAverage(double data){
        buffer[index]=data;
        index++;
    }

    /**
     *设置图表曲线样式
     **/
    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(color);
        r.setPointStyle(style);
        r.setFillPoints(fill);
        r.setLineWidth(2);//这是线宽
        renderer.addSeriesRenderer(r);

        return renderer;
    }

    /**
     * 初始化图表
     * @param renderer
     * @param xTitle
     * @param yTitle
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @param axesColor
     * @param labelsColor
     */
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,
                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        //有关对图表的渲染可参看api文档
        renderer.setChartTitle(title.getText().toString());//设置标题
        renderer.setXAxisMin(xMin);//设置x轴的起始点
        renderer.setXAxisMax(xMax);//设置一屏有多少个点
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setBackgroundColor(Color.BLACK);
        renderer.setLabelsColor(Color.YELLOW);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setShowGrid(true);
        renderer.setGridColor(Color.BLUE);//设置格子的颜色
        renderer.setXLabels(20);//没有什么卵用
        renderer.setYLabels(20);//把y轴刻度平均分成多少个
        renderer.setXTitle(xTitle);//x轴的标题
        renderer.setYTitle(yTitle);//y轴的标题
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setPointSize((float) 2);
        renderer.setShowLegend(false);
    }

    int[] xv = new int[1000];//用来显示的数据
    double[] yv = new double[1000];
    private int addX = -1;
    private double addY = 0;
    /**
     * 更新图表的函数，其实就是重绘
     */
    private void updateChart() {

//        //*********************************************
//        //*********************************************
//        //设置好下一个需要增加的节点
//        addY = AVERAGE;//需要增加的值
//        //移除数据集中旧的点集
//        mDataset.removeSeries(series);
//        //*********************************************
//        //*********************************************

        //判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100



//******************************************************************************************
//******************************************************************************************

//        //每一个新点坐标都后移一位
//        series.add(addX++, addY);//最重要的一句话，以xy对的方式往里放值
//
//        if(addX>100){//如果超出了屏幕边界,实现坐标轴自动移动的方法
//            renderer.setXAxisMin(addX-100);//显示范围为100
//            renderer.setXAxisMax(addX);
//        }
//
//
//        //重要：在数据集中添加新的点集
//        mDataset.addSeries(series);
//
//        视图更新，没有这一步，曲线不会呈现动态
//        如果在非UI主线程中，需要调用postInvalidate()，具体参考api
//        chart.invalidate();
    }
}