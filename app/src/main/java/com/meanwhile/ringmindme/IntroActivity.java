package com.meanwhile.ringmindme;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.meanwhile.ringmindme.provider.action.ActionColumns;
import com.meanwhile.ringmindme.provider.action.ActionContentValues;
import com.meanwhile.ringmindme.provider.action.actionKind;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.Calendar;
import java.util.Date;

public class IntroActivity extends AppCompatActivity implements DateTimeChooserFragment.OnDateTimeFragmentListener, ChooserFragment.OnChooserFragmentListener {

    private static final String RET_SELECTED_TIMEDATE = "selectedDate";
    private static final String TAG = "IntroActivity";
    private InkPageIndicator mIndicator;
    private ViewPager mPager;
    private FloatingActionButton mNextButton;

    private boolean mNeedToGoToLast = true;
    private boolean mRingInside;
    private Date mSelectedDate;


    public static Intent newIntent(Context context) {
        return new Intent(context, IntroActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new IntroPagerAdapter(false, getSupportFragmentManager()));
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

        //init db
        Log.d(TAG, "inserting entries in the database");
        for (int i = 0; i < 20 ; i ++) {

            cal.add(Calendar.DAY_OF_YEAR, take?3:20);
            ActionContentValues values = new ActionContentValues();
            values.putAction(take?actionKind.TAKE:actionKind.REMOVE);
            values.putDate(cal.getTime());
            getContentResolver().insert(ActionColumns.CONTENT_URI, values.values());

            take = !take;
        }

        //finish activity
        //ge got what we need, return date as result and finish
        Intent intent = new Intent();
        intent.putExtra(RET_SELECTED_TIMEDATE, mSelectedDate);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDateTimeSelected(Date date) {
        mSelectedDate = date;

        toogleFab(true);
    }

    @Override
    public void onSelectionMade(boolean ringInside) {
        mRingInside = ringInside;
        mNeedToGoToLast = true;

        //turn FAB into next state
        toogleFab(false);
    }

    private void toogleFab(boolean finishMode) {
        //TODO
    }


    private class IntroPagerAdapter extends FragmentPagerAdapter {
        public IntroPagerAdapter(boolean ringIn, FragmentManager fm) {
            super(fm);
            this.ringIn = ringIn;
        }

        private boolean ringIn;

        public void setRingInside(boolean ringInside) {
            ringIn = ringInside;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return HelloFragment.newInstance();
                case 1:
                    return new ChooserFragment();
                default:
                    return DateTimeChooserFragment.newInstance(ringIn);
            }
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof DateTimeChooserFragment) {
                //recreate fragment with the new value
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
