package com.roselism.spot.library.app.SAM;

import android.content.Context;

import com.roselism.spot.dao.UserOperater;

/**
 * Created by simon on 16-4-14.
 */
public interface SetContext<T extends UserOperater> {
    T setContext(Context context);
}