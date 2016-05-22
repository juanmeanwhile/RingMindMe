package com.meanwhile.ringmindme;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meanwhile.ringmindme.alarm.AlarmHelper;
import com.meanwhile.ringmindme.provider.action.ActionColumns;
import com.meanwhile.ringmindme.provider.action.ActionCursor;
import com.meanwhile.ringmindme.provider.action.ActionSelection;
import com.meanwhile.ringmindme.provider.action.actionKind;
import com.meanwhile.ringmindme.widget.CalendarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tk.zielony.naturaldateformat.NaturalDateFormat;
import tk.zielony.naturaldateformat.RelativeDateFormat;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_GET_TIME_DATE = 1000;
    private CalendarView mCalendar;
    private TextView mActionText;
    private TextView mTimeText;
    private Button mDoneInTimeButton;
    private Button mDoneOutOfTimeButton;
    private int mColorPut = Color.BLUE;
    private int mColorTake = Color.YELLOW;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mColorPut = getResources().getColor(R.color.action_remove);
        mColorTake = getResources().getColor(R.color.action_put);

        mCalendar = (CalendarView) findViewById(R.id.cal);
        mCalendar.getCalendarView().drawSmallIndicatorForEvents(true);

        mActionText = (TextView) findViewById(R.id.action_text);

        mTimeText = (TextView) findViewById(R.id.time_text);

        mDoneInTimeButton = (Button) findViewById(R.id.done_in_time_button);
        mDoneInTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmHelper.setNextAlarm(getApplicationContext());
            }
        });

        mDoneOutOfTimeButton = (Button) findViewById(R.id.done_not_in_time_button);

        boolean showIntro = initView();

        if (showIntro) {
            startActivityForResult(IntroActivity.newIntent(this), REQ_GET_TIME_DATE);
        }
    }

    /***
     * Init views in the activity, returning false if there is no info yet and intro screen has to
     * be showed
     * @return boolean indicating if the intro activity has to be showed
     */
    private boolean initView() {
        ActionSelection where = new ActionSelection();
        where.orderByDate(false);
        where.dateAfter(new Date());
        Cursor c = getContentResolver().query(ActionColumns.CONTENT_URI, null,
                where.sel(), where.args(), where.order());

        ActionCursor cursor = new ActionCursor(c);

        if (cursor.getCount() == 0) {
            return true;
        }

        cursor.moveToFirst();

        mActionText.setText(cursor.getAction().equals(actionKind.PUT)?R.string.next_action_remove:R.string.next_action_take);

        Date actionDate = cursor.getDate();
        RelativeDateFormat relFormat = new RelativeDateFormat(this, NaturalDateFormat.DAYS);
        mTimeText.setText(relFormat.format(actionDate.getTime()));

        //mTimeText.setText(DateUtils.getRelativeTimeSpanString(actionDate.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS));


        //fill calendar data
        List<CalendarDayEvent> events = new ArrayList<CalendarDayEvent>();
        while (!cursor.isAfterLast()) {
            events.add(new CalendarDayEvent(cursor.getDate().getTime(), cursor.getAction().equals(actionKind.TAKE)?mColorTake:mColorPut));

            cursor.moveToNext();
        }
        mCalendar.getCalendarView().addEvents(events);
        cursor.close();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            builder.addTestDevice("2D1E30DEA7AA3061A274961A5B80AF92");
        }
        AdRequest adRequest = builder.build();
        mAdView.loadAd(adRequest);

        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public void onMenuSettings(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "menu_settings");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "menu_settings");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "menu_option");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GET_TIME_DATE) {
            if (resultCode == RESULT_OK) {
                initView();
            }
        }
    }
}
