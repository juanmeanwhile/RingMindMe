package com.meanwhile.ringmindme;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pixelcan.inkpageindicator.InkPageIndicator;

public class IntroActivity extends AppCompatActivity {

    private InkPageIndicator mIndicator;
    private ViewPager mPager;
    private FloatingActionButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));

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
                    //grab date, init db

                    //end Intro

                }
            }
        });
    }

    private class IntroPagerAdapter extends FragmentPagerAdapter {
        public IntroPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
//                case 1:
//                    return HelloFragment.newInstance();
//                case 2:
//                    return InOutFragment.newInstance();
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
