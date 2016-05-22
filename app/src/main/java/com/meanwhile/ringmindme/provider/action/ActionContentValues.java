package com.meanwhile.ringmindme.provider.action;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.meanwhile.ringmindme.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code action} table.
 */
public class ActionContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return ActionColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable ActionSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable ActionSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public ActionContentValues putAction(@NonNull actionKind value) {
        if (value == null) throw new IllegalArgumentException("action must not be null");
        mContentValues.put(ActionColumns.ACTION, value.ordinal());
        return this;
    }


    /**
     * Date when the action has to be performed
     */
    public ActionContentValues putDate(@NonNull Date value) {
        if (value == null) throw new IllegalArgumentException("date must not be null");
        mContentValues.put(ActionColumns.DATE, value.getTime());
        return this;
    }


    public ActionContentValues putDate(long value) {
        mContentValues.put(ActionColumns.DATE, value);
        return this;
    }

    /**
     * If the action has been already performed
     */
    public ActionContentValues putReady(boolean value) {
        mContentValues.put(ActionColumns.READY, value);
        return this;
    }

}
