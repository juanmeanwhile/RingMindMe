package com.meanwhile.ringmindme;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meanwhile.ringmindme.alarm.AlarmHelper;
import com.meanwhile.ringmindme.provider.action.ActionColumns;
import com.meanwhile.ringmindme.provider.action.ActionContentValues;
import com.meanwhile.ringmindme.provider.action.ActionCursor;
import com.meanwhile.ringmindme.provider.action.ActionSelection;
import com.meanwhile.ringmindme.provider.action.actionKind;
import com.meanwhile.ringmindme.ui.CalendarView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tk.zielony.naturaldateformat.NaturalDateFormat;
import tk.zielony.naturaldateformat.RelativeDateFormat;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int REQ_GET_TIME_DATE = 1000;
    private static final long ONE_DAY = 24 * 3600 * 1000;
    private static final int DAYS_TO_PUT = 7;
    private static final int DAYS_TO_TAKE = 21;
    private CalendarView mCalendar;
    private TextView mActionText;
    private TextView mTimeText;
    private Button mDoneInTimeButton;
    private Button mDoneOutOfTimeButton;
    private ImageView mImageView;
    private long mCurrentActionId;
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
                //cancel current alarm
                AlarmHelper.cancelAlarms(MainActivity.this);

                //mark as done
                ActionContentValues values = new ActionContentValues();
                values.putReady(true);
                ActionSelection where = new ActionSelection();
                where.id(mCurrentActionId);
                getContentResolver().update(ActionColumns.CONTENT_URI, values.values(), ActionColumns._ID + "=?", new String[]{""+mCurrentActionId});

                //set next alarm
                AlarmHelper.setNextAlarm(MainActivity.this);

                insertNewActionAtTheEnd();
            }
        });


        mDoneOutOfTimeButton = (Button) findViewById(R.id.done_not_in_time_button);
        mDoneOutOfTimeButton.setVisibility(View.GONE);

        //Configure ads
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            builder.addTestDevice("2D1E30DEA7AA3061A274961A5B80AF92");
        }
        AdRequest adRequest = builder.build();
        mAdView.loadAd(adRequest);

        //get some analytics
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

//        //Show intro screen if needed
//        if (showIntro) {
//            startActivityForResult(IntroActivity.newIntent(this), REQ_GET_TIME_DATE);
//        }

        getLoaderManager().initLoader(0, null, this);

        mImageView = (ImageView) findViewById(R.id.background);
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                new GetBackgrounfAsyncTask(mImageView.getHeight(), mImageView.getWidth()).execute();
            }
        });

    }

    private void insertNewActionAtTheEnd() {
        //create a new action at the end
        ActionSelection where = new ActionSelection();
        where.orderByDate(false);
        Cursor c = getContentResolver().query(ActionColumns.CONTENT_URI, null, where.sel(), where.args(), where.order() );
        ActionCursor cursor = new ActionCursor(c);
        cursor.moveToLast();

        ActionContentValues values = new ActionContentValues();
        values.putAction(cursor.getAction().equals(actionKind.TAKE)?actionKind.PUT: actionKind.TAKE);
        values.putReady(false);
        Calendar lastCal = Calendar.getInstance();
        lastCal.setTime(cursor.getDate());
        lastCal.add(Calendar.DAY_OF_YEAR, cursor.getAction().equals(actionKind.TAKE)?DAYS_TO_PUT: DAYS_TO_TAKE );
        values.putDate(lastCal.getTime());
        getContentResolver().insert(ActionColumns.CONTENT_URI, values.values());
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

        startActivity(new Intent(this, SettingsActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQ_GET_TIME_DATE) {
//            if (resultCode == RESULT_OK) {
//                initView();
//            }
//        }
    }

    private void populateViews(Cursor data) {
        ActionCursor cursor = new ActionCursor(data);

        //remove already set events
        mCalendar.getCalendarView().removeAllEvents();

        if (cursor.getCount() == 0) {
            return;
        }

        cursor.moveToFirst();
        mCurrentActionId = cursor.getId();
        mActionText.setText(cursor.getAction().equals(actionKind.PUT)?R.string.next_action_put :R.string.next_action_take);
        Date actionDate = cursor.getDate();
        RelativeDateFormat relFormat = new RelativeDateFormat(this, NaturalDateFormat.DAYS);
        mTimeText.setText(relFormat.format(actionDate.getTime()));

        //show done button if we are in the current day
        mDoneInTimeButton.setVisibility(actionDate.before(new Date(System.currentTimeMillis() + ONE_DAY))?View.VISIBLE:View.GONE);

        //fill calendar data
        List<CalendarDayEvent> events = new ArrayList<CalendarDayEvent>();
        while (!cursor.isAfterLast()) {
            events.add(new CalendarDayEvent(cursor.getDate().getTime(), cursor.getAction().equals(actionKind.TAKE)?mColorTake:mColorPut));
            cursor.moveToNext();
        }
        mCalendar.getCalendarView().addEvents(events);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ActionSelection where = new ActionSelection();
        where.orderByDate(false);
        where.ready(false);

        return new CursorLoader(this, ActionColumns.CONTENT_URI, null, where.sel(), where.args(), where.order());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        populateViews(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //
    }

    public class GetBackgrounfAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        private int height;
        private int width;
        public GetBackgrounfAsyncTask(int height, int width) {
            this.height = height;
            this.width = width;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                //Use picasso synchronously because I want to have an asynctask running to accomplish the rubric ^^
                return Picasso.with(MainActivity.this)
                       .load("https://source.unsplash.com/random")
                       .resize(width, height)
                       .centerCrop()
                       .get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }
}
