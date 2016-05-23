package com.meanwhile.ringmindme.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.meanwhile.ringmindme.MainActivity;
import com.meanwhile.ringmindme.R;
import com.meanwhile.ringmindme.provider.action.ActionColumns;
import com.meanwhile.ringmindme.provider.action.ActionCursor;
import com.meanwhile.ringmindme.provider.action.ActionSelection;
import com.meanwhile.ringmindme.provider.action.actionKind;

import java.text.SimpleDateFormat;
import java.util.Date;

import tk.zielony.naturaldateformat.NaturalDateFormat;
import tk.zielony.naturaldateformat.RelativeDateFormat;


public class ActionAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        //Get data from content resolver
        ActionSelection where = new ActionSelection();
        where.orderByDate(false);
        where.ready(false);
        Date fragmentdate = new Date(System.currentTimeMillis());

        Cursor c = context.getContentResolver().query(ActionColumns.CONTENT_URI, null,
                where.sel(), where.args(), where.order());
        ActionCursor cursor = new ActionCursor(c);
        cursor.moveToFirst();

        RelativeDateFormat relFormat = new RelativeDateFormat(context, NaturalDateFormat.DAYS);
        Date actionDate = null;
        if (cursor.getCount() > 0) {
            actionDate = cursor.getDate();
        }

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            rv.setOnClickPendingIntent(R.id.root, pendingIntent);

            if (actionDate != null) {
                rv.setTextViewText(R.id.action_text, context.getString(cursor.getAction().equals(actionKind.PUT)? R.string.next_action_put :R.string
                        .next_action_take));
                rv.setTextViewText(R.id.time_text, relFormat.format(actionDate.getTime()));

            } else {
                rv.setTextViewText(R.id.action_text, context.getString(R.string.widget_empty));
            }
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}