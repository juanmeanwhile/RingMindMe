package com.meanwhile.ringmindme.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.meanwhile.ringmindme.provider.action.ActionColumns;
import com.meanwhile.ringmindme.provider.action.ActionCursor;
import com.meanwhile.ringmindme.provider.action.ActionSelection;

import java.util.Date;

/**
 * Created by Juan on 21/05/2016.
 */
public class AlarmHelper {

    public static void setNextAlarm(Context context) {
        //get next alarm to be set
        ActionSelection where = new ActionSelection();
        where.orderByDate(false);
        where.dateAfter(new Date());
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
        alarmManager.cancel(alarmIntent);

        //set new alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
    }

}
