package com.example.alinearchart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
    private Button lineChartBtn;
    private Button realtime_lineChartBtn;
    private Button realTimeLineChartBtn;
//    private Button areaChartBtn;
//    private Button scatterChartBtn;
//    private Button timeChartBtn;
//    private Button barChartBtn;
//    private Button pieChartBtn;
//    private Button bubbleChartBtn;
//    private Button doughnutChartBtn;
//    private Button rangeBarChartBtn;
//    private Button dialChartBtn;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChartBtn=(Button) findViewById(R.id.line_chart);
        realtime_lineChartBtn=(Button) findViewById(R.id.realtime_line_chart);
        realTimeLineChartBtn=(Button) findViewById(R.id.second_realtime_btn);
//        areaChartBtn=(Button) findViewById(R.id.area_chart);
//        scatterChartBtn=(Button) findViewById(R.id.scatter_chart);
//        timeChartBtn=(Button) findViewById(R.id.time_chart);
//        barChartBtn=(Button) findViewById(R.id.bar_chart);
//        pieChartBtn=(Button) findViewById(R.id.pie_chart);
//        bubbleChartBtn=(Button) findViewById(R.id.bubble_chart);
//        doughnutChartBtn=(Button) findViewById(R.id.doughnut_chart);
//        rangeBarChartBtn=(Button) findViewById(R.id.range_bar_chart);
//        dialChartBtn=(Button) findViewById(R.id.dial_chart);
        OnClickListener listener=new AChartEngineListener();

        lineChartBtn.setOnClickListener(listener);
        realtime_lineChartBtn.setOnClickListener(listener);
        realTimeLineChartBtn.setOnClickListener(listener);

//        pieChartBtn.setOnClickListener(listener);
//        barChartBtn.setOnClickListener(listener);
//        bubbleChartBtn.setOnClickListener(listener);
    }
    class AChartEngineListener implements OnClickListener{
        @Override
        public void onClick(View v) {
// TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.line_chart:
                    Intent intent = new Intent(MainActivity.this, LinearChartActivity.class);
                    startActivity(intent);
                    break;
                case R.id.realtime_line_chart:
                        Intent rt_intent= new Intent(MainActivity.this, AccelActivity.class);
                        startActivity(rt_intent);
                        break;
                case R.id.second_realtime_btn:
                    Intent second_intent = new Intent(MainActivity.this, RealTimeLineChartActivity.class);
                    startActivity(second_intent);

//                case R.id.area_chart:
//                    break;
//                case R.id.scatter_chart:
//                    break;
//                case R.id.time_chart:
//                    break;
//                case R.id.bar_chart:
//                    Intent barChartIntent=new ABarChart().execute(MainActivity.this);
//                    startActivity(barChartIntent);
//                    break;
//                case R.id.pie_chart:
//                    Intent pieChartIntent=new APieChart().execute(MainActivity.this);
//                    startActivity(pieChartIntent);
//                    break;
//                case R.id.bubble_chart:
//                    Intent bubbleChartIntent=new ABubbleChart().execute(MainActivity.this);
//                    startActivity(bubbleChartIntent);
//                    break;
//                case R.id.doughnut_chart:
//                    break;
//                case R.id.range_bar_chart:
//                    break;
//                case R.id.dial_chart:
//                    break;
//                default:
//                    break;
            }
        }
    }
}