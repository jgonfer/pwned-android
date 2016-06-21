package com.jgonfer.pwned.model;

import com.jgonfer.pwned.model.realm.BreachedService;

/**
 * Created by jgonfer on 12/6/16.
 */
public class BreachedServiceDrawerItem {

    private BreachedService mBreachedService;
    private boolean mIsFixed;
    private int mPosition;

    public BreachedServiceDrawerItem() {

    }

    public BreachedServiceDrawerItem(BreachedService breachedService, boolean isFixed, int position) {
        this.mBreachedService = breachedService;
        this.mIsFixed = isFixed;
        this.mPosition = position;
    }

    public BreachedService getBreachedService() {
        return mBreachedService;
    }

    public void setBreachedService(BreachedService mBreachedService) {
        this.mBreachedService = mBreachedService;
    }

    public boolean isFixed() {
        return mIsFixed;
    }

    public void setIsFixed(boolean mIsFixed) {
        this.mIsFixed = mIsFixed;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
