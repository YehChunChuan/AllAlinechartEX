package com.example.alinearchart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import org.achartengine.chart.PointStyle;

import java.util.Random;

public class RealTimeLineChartActivity extends Activity {

    private LineChart mLineChart;//直線圖類
    private boolean addData_thread_run; // 控制新增折線圖資料執行緒的退出
    private int x_index;// X軸的刻度值
    private Random random;// 用來獲取隨機數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart);//設定主介面

        //建立折線圖例項 （X軸標題，Y軸標題，X軸的最小值，X軸的最大值，Y軸的最小值,Y軸的最大值，座標軸的顏色，刻度值的顏色）
        mLineChart = new LineChart("時間(min)", "", 0, 100, 0, 9500, Color.WHITE, Color.WHITE);

        x_index = 110;//初始化X軸的刻度值
        random = new Random();
    };

    @Override
    protected void onResume() //在本頁面onStart()之後設定為繪圖所在的頁面
    {
        super.onResume();

        //設定圖表顯示頁面為本頁面
        mLineChart.setChartViewActivity(this);

        //新增4條折線
        mLineChart.addLineToChart("折線A", PointStyle.CIRCLE, Color.BLUE);//新增折線A
        mLineChart.addLineToChart("折線B", PointStyle.DIAMOND, Color.GREEN);//新增折線B
        mLineChart.addLineToChart("折線C", PointStyle.TRIANGLE, Color.CYAN);//新增折線C
        mLineChart.addLineToChart("折線D", PointStyle.SQUARE, Color.YELLOW);//新增折線D

    }

    // 訊息控制代碼(執行緒裡無法進行介面更新，所以要把訊息從執行緒裡傳送出來在訊息控制代碼裡進行處理)
    public Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    //新增資料 (新增折點)
                    mLineChart.addData("折線A", x_index, random.nextInt(9000) );
                    mLineChart.addData("折線B", x_index, random.nextInt(9000) );
                    mLineChart.addData("折線C", x_index, random.nextInt(9000) );
                    mLineChart.addData("折線D", x_index, random.nextInt(9000) );


                    x_index += 10; // X軸每次右移10個刻度
                    mLineChart.moveChart(10);// 圖示右移10刻度值

                    //繪製折線圖(更新UI)
                    mLineChart.mChartView.repaint();
                    break;
            }
        }
    };

    // "開始"按鈕的點選響應動作
    public void onButtonStartClicked(View v)
    {
        Button btn_start = (Button)v;// 拿到按鈕控制代碼
        if( btn_start.getText().toString().equals("開始") )
        {// 點選的是"開始"
            new Thread(addData_thread).start() ;// 開啟子執行緒,開始動態的新增資料
            btn_start.setText("停止");// 設定按鈕文字為 "停止"
        }
        else // 點選的是"停止"
        {
            addData_thread_run = false;// 結束子執行緒,停止新增資料
            btn_start.setText("開始");// 設定按鈕文字為 "開始"
        }

    }

    //新增折線圖資料的執行緒
    private Runnable addData_thread = new Runnable()
    {
        @Override
        public void run()
        {

            addData_thread_run = true;
            while(addData_thread_run)
            {
                try {
                    Thread.sleep(10);// 延時1秒
                    mHandler.sendEmptyMessage(0);// 傳送0型別資訊，通知主執行緒更新圖表

                } catch (InterruptedException e)
                {
                    break;
                }
            }
        }
    };

}//end with MainActivity
