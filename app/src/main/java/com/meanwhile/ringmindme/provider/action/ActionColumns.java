package com.meanwhile.ringmindme.provider.action;

import android.net.Uri;
import android.provider.BaseColumns;

import com.meanwhile.ringmindme.provider.ActionProvider;
import com.meanwhile.ringmindme.provider.action.ActionColumns;

/**
 * An action to be performed by th euser
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


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            ACTION,
            DATE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(ACTION) || c.contains("." + ACTION)) return true;
            if (c.equals(DATE) || c.contains("." + DATE)) return true;
        }
        return false;
    }

}
