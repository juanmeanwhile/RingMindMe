package com.meanwhile.ringmindme.provider.action;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.meanwhile.ringmindme.provider.base.AbstractSelection;

/**
 * Selection for the {@code action} table.
 */
public class ActionSelection extends AbstractSelection<ActionSelection> {
    @Override
    protected Uri baseUri() {
        return ActionColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code ActionCursor} object, which is positioned before the first entry, or null.
     */
    public ActionCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new ActionCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public ActionCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code ActionCursor} object, which is positioned before the first entry, or null.
     */
    public ActionCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new ActionCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public ActionCursor query(Context context) {
        return query(context, null);
    }


    public ActionSelection id(long... value) {
        addEquals("action." + ActionColumns._ID, toObjectArray(value));
        return this;
    }

    public ActionSelection idNot(long... value) {
        addNotEquals("action." + ActionColumns._ID, toObjectArray(value));
        return this;
    }

    public ActionSelection orderById(boolean desc) {
        orderBy("action." + ActionColumns._ID, desc);
        return this;
    }

    public ActionSelection orderById() {
        return orderById(false);
    }

    public ActionSelection action(actionKind... value) {
        addEquals(ActionColumns.ACTION, value);
        return this;
    }

    public ActionSelection actionNot(actionKind... value) {
        addNotEquals(ActionColumns.ACTION, value);
        return this;
    }


    public ActionSelection orderByAction(boolean desc) {
        orderBy(ActionColumns.ACTION, desc);
        return this;
    }

    public ActionSelection orderByAction() {
        orderBy(ActionColumns.ACTION, false);
        return this;
    }

    public ActionSelection date(Date... value) {
        addEquals(ActionColumns.DATE, value);
        return this;
    }

    public ActionSelection dateNot(Date... value) {
        addNotEquals(ActionColumns.DATE, value);
        return this;
    }

    public ActionSelection date(long... value) {
        addEquals(ActionColumns.DATE, toObjectArray(value));
        return this;
    }

    public ActionSelection dateAfter(Date value) {
        addGreaterThan(ActionColumns.DATE, value);
        return this;
    }

    public ActionSelection dateAfterEq(Date value) {
        addGreaterThanOrEquals(ActionColumns.DATE, value);
        return this;
    }

    public ActionSelection dateBefore(Date value) {
        addLessThan(ActionColumns.DATE, value);
        return this;
    }

    public ActionSelection dateBeforeEq(Date value) {
        addLessThanOrEquals(ActionColumns.DATE, value);
        return this;
    }

    public ActionSelection orderByDate(boolean desc) {
        orderBy(ActionColumns.DATE, desc);
        return this;
    }

    public ActionSelection orderByDate() {
        orderBy(ActionColumns.DATE, false);
        return this;
    }
}
