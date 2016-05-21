package com.meanwhile.ringmindme.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.meanwhile.ringmindme.BuildConfig;
import com.meanwhile.ringmindme.provider.action.ActionColumns;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = MySQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "ringmind.db";
    private static final int DATABASE_VERSION = 1;
    private static MySQLiteOpenHelper sInstance;
    private final Context mContext;
    private final MySQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_ACTION = "CREATE TABLE IF NOT EXISTS "
            + ActionColumns.TABLE_NAME + " ( "
            + ActionColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ActionColumns.ACTION + " INTEGER NOT NULL, "
            + ActionColumns.DATE + " INTEGER NOT NULL "
            + " );";

    // @formatter:on

    public static MySQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static MySQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static MySQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new MySQLiteOpenHelper(context);
    }

    private MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new MySQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static MySQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new MySQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private MySQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new MySQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_ACTION);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
