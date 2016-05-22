package com.meanwhile.ringmindme.provider.action;

import android.net.Uri;
import android.provider.BaseColumns;

import com.meanwhile.ringmindme.provider.ActionProvider;
import com.meanwhile.ringmindme.provider.action.ActionColumns;

/**
 * An action to be performed by the user
 */
public class ActionColumns implements BaseColumns {
    public static final String TABLE_NAME = "action";
    public static final Uri CONTENT_URI = Uri.parse(ActionProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String ACTION = "action";

    /**
     * Date when the action has to be performed
     */
    public static final String DATE = "date";

    /**
     * If the action has been already performed
     */
    public static final String READY = "ready";


    public static final String DEFAULT_ORDER = TABLE_NAME + "build/intermediates/exploded-aar/com.pacioianu.david/ink-page-indicator/1.1.1/res" +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            ACTION,
            DATE,
            READY
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(ACTION) || c.contains("build/intermediates/exploded-aar/com.pacioianu.david/ink-page-indicator/1.1.1/res" + ACTION)) return true;
            if (c.equals(DATE) || c.contains("build/intermediates/exploded-aar/com.pacioianu.david/ink-page-indicator/1.1.1/res" + DATE)) return true;
            if (c.equals(READY) || c.contains("build/intermediates/exploded-aar/com.pacioianu.david/ink-page-indicator/1.1.1/res" + READY)) return true;
        }
        return false;
    }

}
