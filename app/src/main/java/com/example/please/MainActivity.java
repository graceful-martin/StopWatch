package com.example.please;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private TextView timeText;
    private TextView timeTable;
    private Button leftButton;
    private Button rightButton;
    private Toast mToast = null;
    private Handler mhandler = new Handler();
    private ScrollView timeScroll;
    private long mstarttime = 0L;
    private MyRunnable runnable = new MyRunnable();
    private boolean isRunning = false;
    private ArrayList<String> timeList = new ArrayList<>();
    private int rapCount = 0;
    private long ms, sec, min, hour, sub_ms = 0, sub_sec = 0, sub_min = 0, sub_hour = 0;;
    private String ms_s, sec_s, min_s, hour_s;

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            if (isRunning) {
                long t = SystemClock.elapsedRealtime();
                long lab = t - mstarttime;


                ms = (sub_ms + lab) % 100;
                ms_s = "" + ms;
                if (ms < 10)
                    ms_s = "0" + ms;
                else if (ms < 100)
                    ms_s = "" + ms;

                sec = sub_sec + (lab / 1000) % 60;
                sec_s = "" + sec;
                if (sec < 10)
                    sec_s = "0" + sec;
                min = sub_min + (sec / 60) % 60;
                min_s = "" + min;
                if (min < 60)
                    min_s = "0" + min;
                hour = sub_hour + (min / 60) % 24;
                hour_s = "" + hour;
                if (hour < 10)
                    hour_s = "0" + hour;
                timeText.setText(hour_s + ":" + min_s + ":" + sec_s + "." + ms_s);
                mhandler.post(this);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("OwnWatch");
        setContentView(R.layout.activity_main);

        timeText = findViewById(R.id.timeText);
        timeTable = findViewById(R.id.timeTable);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        //timeScroll = (ScrollView) findViewById(R.id.timeScroll);

        toastShort("OwnWatch");


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftButton.getText().equals(new String("시작"))) {
                    timeText.setText("00:00:00.00");
                    sub_ms = 0;
                    sub_sec = 0;
                    sub_min = 0;
                    sub_hour = 0;
                    timeTable.setText("");
                    timeList = new ArrayList<>();
                } else if (leftButton.getText().equals(new String("랩"))) {
                    if (timeList.size() > 20)
                        timeList.remove(0);
                    timeList.add(timeText.getText().toString());
                    timeTable.setText("");
                    Collections.sort(timeList);
                    for (String time : timeList) {
                        rapCount++;
                        timeTable.append("랩." + rapCount + " " + time + "\n");
                    }
                    rapCount = 0;
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightButton.getText().equals(new String("재설정"))) {
                    leftButton.setText("랩");
                    rightButton.setText("중단");
                    isRunning = true;
                    mstarttime = SystemClock.elapsedRealtime();
                    mhandler.post(runnable);


                } else if (rightButton.getText().equals(new String("중단"))) {
                    leftButton.setText("재설정");
                    rightButton.setText("시작");
                    isRunning = false;
                    sub_ms = ms;
                    sub_sec = sec;
                    sub_min = min;
                    sub_hour = hour;
                }

            }
        });
    }

    private void scrollRun() {
        timeScroll.post(new Runnable() {
            @Override
            public void run() {
                timeScroll.smoothScrollTo(0, timeTable.getBottom());
            }
        });
    }

    public void toastShort(String mToastStr) {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, mToastStr, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(mToastStr);
        }
        mToast.show();
    }
}
