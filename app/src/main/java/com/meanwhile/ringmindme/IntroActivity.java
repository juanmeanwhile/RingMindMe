package com.meanwhile.ringmindme;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.meanwhile.ringmindme.alarm.AlarmHelper;
import com.meanwhile.ringmindme.provider.action.ActionColumns;
import com.meanwhile.ringmindme.provider.action.ActionContentValues;
import com.meanwhile.ringmindme.provider.action.actionKind;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.Calendar;
import java.util.Date;

public class IntroActivity extends AppCompatActivity implements DateTimeChooserFragment.OnDateTimeFragmentListener, ChooserFragment.OnChooserFragmentListener {

    private static final String RET_SELECTED_TIMEDATE = "selectedDate";
    private static final String TAG = "IntroActivity";
    private static final int DAYS_TO_PUT = 7;
    private static final int DAYS_TO_TAKE = 21;
    private static final String INITIALIZED = "com.meanwhile.ringmind.initialized";
    private InkPageIndicator mIndicator;
    private ViewPager mPager;
    private FloatingActionButton mNextButton;
    private IntroPagerAdapter mAdapter;

    private boolean mNeedToGoToLast = true;
    private Boolean mRingInside;
    private Date mSelectedDate;


    public static Intent newIntent(Context context) {
        return new Intent(context, IntroActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(INITIALIZED, false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new IntroPagerAdapter(false, getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNeedToGoToLast = position != 2;
                toogleFab(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mNextButton = (FloatingActionButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mSelectedDate != null && !mNeedToGoToLast) {
                    endIntro();
                }
                if (mPager.getCurrentItem() < 2) {
                    //move to next
                    mPager.setCurrentItem(mPager.getCurrentItem()+1);
                }
            }
        });

    }

    private void endIntro() {
        boolean take = mRingInside;
        Calendar cal = Calendar.getInstance();
        cal.setTime(mSelectedDate);
        cal.getTime();

        Date firstAlarm = null;

        //init db
        Log.d(TAG, "inserting entries in the database");
        for (int i = 0; i < 20 ; i++) {

            cal.add(Calendar.DAY_OF_YEAR, take?DAYS_TO_TAKE:DAYS_TO_PUT);
            ActionContentValues values = new ActionContentValues();
            values.putAction(take?actionKind.TAKE:actionKind.PUT);
            values.putDate(cal.getTime());
            values.putReady(false);
            getContentResolver().insert(ActionColumns.CONTENT_URI, values.values());

            take = !take;

            if (i == 0) {
                firstAlarm = cal.getTime();
            }
        }

        //set first alarm
        AlarmHelper.setAlarm(this, firstAlarm);

        //Don't show intro again
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(INITIALIZED, true).commit();

        //launch Main Activity
        startActivity(new Intent(this, MainActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @Override
    public void onDateTimeSelected(Date date) {
        mSelectedDate = date;

        toogleFab(true);
    }

    @Override
    public void onSelectionMade(boolean ringInside) {
        mNeedToGoToLast = true;
        mRingInside = ringInside;

        //turn FAB into next state
        toogleFab(false);
        mAdapter.setRingInside(ringInside);
    }

    private void toogleFab(boolean finishMode) {
        //TODO
    }


    private class IntroPagerAdapter extends FragmentPagerAdapter {
        DateTimeChooserFragment mDateFragment;
        public IntroPagerAdapter(boolean ringIn, FragmentManager fm) {
            super(fm);
            this.ringIn = ringIn;
        }

        private boolean ringIn;

        public void setRingInside(boolean ringInside) {
            ringIn = ringInside;
             if (mDateFragment != null) {
                 mDateFragment.updateRingQuestion(ringIn);
             }
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return HelloFragment.newInstance();
                case 1:
                    return new ChooserFragment();
                default:
                    mDateFragment=  DateTimeChooserFragment.newInstance(ringIn);
                    return mDateFragment;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof DateTimeChooserFragment) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
