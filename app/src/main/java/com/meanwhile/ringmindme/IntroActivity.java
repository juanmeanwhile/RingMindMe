package com.meanwhile.ringmindme;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.Date;

public class IntroActivity extends AppCompatActivity implements DateTimeChooserFragment.OnDateTimeFragmentListener, ChooserFragment.OnChooserFragmentListener {

    private static final String RET_SELECTED_TIMEDATE = "selectedDate";
    private InkPageIndicator mIndicator;
    private ViewPager mPager;
    private FloatingActionButton mNextButton;
    private boolean mIn;

    private Date mSelectedDate;

    public static Intent newIntent(Context context) {
        return new Intent(context, IntroActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));

        mIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mNextButton = (FloatingActionButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() < 2) {
                    //move to next

                } else  if (mPager.getCurrentItem() == 2){
                    //move to next

                    //animate FAB to check state
                } else {
                    //ge got what we need, return date as result and finish
                    Intent intent = new Intent();
                    intent.putExtra(RET_SELECTED_TIMEDATE, mSelectedDate);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                mPager.setCurrentItem(mPager.getCurrentItem()+1);
            }
        });
    }

    @Override
    public void onDateTimeSelected(Date date) {
        mSelectedDate = date;
    }

    @Override
    public void onSelectionMade(boolean alreadyIn) {
        mIn = alreadyIn;
    }

    private class IntroPagerAdapter extends FragmentPagerAdapter {
        public IntroPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return HelloFragment.newInstance();
                case 1:
                    return new ChooserFragment();
                default:
                    return DateTimeChooserFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
