package com.meanwhile.ringmindme.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.meanwhile.ringmindme.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Juan on 22/05/2016.
 */
public class CalendarView extends LinearLayout {

    public interface OnDaySelectedListener {
        void onDaySelected(Date dateClicked);
    }

    private CompactCalendarView mCalendarView;
    private TextView mMonthview;
    private SimpleDateFormat mDf;
    private OnDaySelectedListener mListener;


    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        mDf = new SimpleDateFormat("MMMM");

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_calendar, this, true);

        mMonthview = (TextView) v.findViewById(R.id.month);
        mMonthview.setText(mDf.format(new Date()));

        mCalendarView = (CompactCalendarView) v.findViewById(R.id.calendar);
        mCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                if (mListener != null) {
                    mListener.onDaySelected(dateClicked);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mMonthview.setText(mDf.format(firstDayOfNewMonth));
            }
        });
    }

    public void setListener(OnDaySelectedListener mListener) {
        this.mListener = mListener;
    }

    public CompactCalendarView getCalendarView() {
        return mCalendarView;
    }
}
