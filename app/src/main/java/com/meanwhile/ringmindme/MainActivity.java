package com.meanwhile.ringmindme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_GET_TIME_DATE = 1000;
    private CompactCalendarView mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendar = (CompactCalendarView) findViewById(R.id.cal);

        startActivityForResult(IntroActivity.newIntent(this), REQ_GET_TIME_DATE);
    }
}
