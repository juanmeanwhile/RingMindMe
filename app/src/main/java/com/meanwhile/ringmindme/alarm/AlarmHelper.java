package com.meanwhile.ringmindme.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.meanwhile.ringmindme.AlarmReceiver;
import com.meanwhile.ringmindme.provider.action.ActionColumns;
import com.meanwhile.ringmindme.provider.action.ActionCursor;
import com.meanwhile.ringmindme.provider.action.ActionSelection;

import java.util.Date;

/**
 * Created by Juan on 21/05/2016.
 */
public class AlarmHelper {

    private static final String TAG = "AlarmHelper";

    public static void setNextAlarm(Context context) {
        //get next alarm to be set
        ActionSelection where = new ActionSelection();
        where.orderByDate(false);
        where.ready(false);
        Cursor c = context.getContentResolver().query(ActionColumns.CONTENT_URI, null,
                where.sel(), where.args(), where.order());

        ActionCursor cursor = new ActionCursor(c);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            setAlarm(context,cursor.getDate());
        }
    }

    public static void setAlarm(Context context, Date date){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);

        //Cancel previously set alarm
        //alarmManager.cancel(alarmIntent);

        //set new alarm
        //alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date.getTime(),
                AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);

        Log.d(TAG, "Alarm set at: " + date.toString());
    }

    public static void setAlarmInTheNextMinute(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.meanwhile.ringmindme.ALARM");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Cancel previously set alarm
        //alarmManager.cancel(alarmIntent);

        Date date = new Date(System.currentTimeMillis() + 20000);

        //set new alarm
        //alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date.getTime() ,
                60000, alarmIntent);

        Log.d(TAG, "Alarm set at: " + date.toString());
    }

    public static void cancelAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.meanwhile.ringmindme.ALARM");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(alarmIntent);
    }
}
