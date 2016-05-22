package com.meanwhile.ringmindme.provider.action;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.meanwhile.ringmindme.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code action} table.
 */
public class ActionCursor extends AbstractCursor implements ActionModel {
    public ActionCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(ActionColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code action} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public actionKind getAction() {
        Integer intValue = getIntegerOrNull(ActionColumns.ACTION);
        if (intValue == null)
            throw new NullPointerException("The value of 'action' in the database was null, which is not allowed according to the model definition");
        return actionKind.values()[intValue];
    }

    /**
     * Date when the action has to be performed
     * Cannot be {@code null}.
     */
    @NonNull
    public Date getDate() {
        Date res = getDateOrNull(ActionColumns.DATE);
        if (res == null)
            throw new NullPointerException("The value of 'date' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * If the action has been already performed
     */
    public boolean getReady() {
        Boolean res = getBooleanOrNull(ActionColumns.READY);
        if (res == null)
            throw new NullPointerException("The value of 'ready' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
