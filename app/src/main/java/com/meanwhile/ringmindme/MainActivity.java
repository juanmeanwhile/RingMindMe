package com.meanwhile.ringmindme;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;
import com.meanwhile.ringmindme.provider.action.ActionColumns;
import com.meanwhile.ringmindme.provider.action.ActionCursor;
import com.meanwhile.ringmindme.provider.action.ActionSelection;
import com.meanwhile.ringmindme.provider.action.actionKind;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tk.zielony.naturaldateformat.NaturalDateFormat;
import tk.zielony.naturaldateformat.RelativeDateFormat;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_GET_TIME_DATE = 1000;
    private CompactCalendarView mCalendar;
    private TextView mActionText;
    private TextView mTimeText;
    private int mColorRemove = Color.BLUE;
    private int mColorTake = Color.YELLOW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendar = (CompactCalendarView) findViewById(R.id.cal);

        mActionText = (TextView) findViewById(R.id.action_text);

        mTimeText = (TextView) findViewById(R.id.time_text);

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

        mActionText.setText(cursor.getAction().equals(actionKind.REMOVE)?R.string.next_action_remove:R.string.next_action_take);

        Date actionDate = cursor.getDate();
        RelativeDateFormat relFormat = new RelativeDateFormat(this, NaturalDateFormat.DAYS | NaturalDateFormat.HOURS);
        mTimeText.setText(relFormat.format(actionDate.getTime()));

        //fill calendar data
        List<CalendarDayEvent> events = new ArrayList<CalendarDayEvent>();
        while (!cursor.isAfterLast()) {
            events.add(new CalendarDayEvent(cursor.getDate().getTime(), cursor.getAction().equals(actionKind.REMOVE)?mColorRemove:mColorTake));

            cursor.moveToNext();
        }
        mCalendar.addEvents(events);

        return false;
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
