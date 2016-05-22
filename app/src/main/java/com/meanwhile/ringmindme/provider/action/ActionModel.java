package com.meanwhile.ringmindme.provider.action;

import com.meanwhile.ringmindme.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * An action to be performed by the user
 */
public interface ActionModel extends BaseModel {

    /**
     * Get the {@code action} value.
     * Cannot be {@code null}.
     */
    @NonNull
    actionKind getAction();

    /**
     * Date when the action has to be performed
     * Cannot be {@code null}.
     */
    @NonNull
    Date getDate();

    /**
     * If the action has been already performed
     */
    boolean getReady();
}
