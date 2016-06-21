package com.jgonfer.pwned.model;

import android.content.Context;

import com.jgonfer.pwned.R;

/**
 * Created by jgonfer on 12/6/16.
 */
public class InfoTipDrawerItem {
    private InfoTip mType;
    private Context mContext;
    private int mTotalBreaches;

    public InfoTipDrawerItem() {

    }

    public InfoTipDrawerItem(InfoTip type, Context context, int totalBreaches) {
        this.mType = type;
        this.mContext = context;
        this.mTotalBreaches = totalBreaches;
    }

    public InfoTip getType() {
        return mType;
    }

    public void setType(InfoTip mType) {
        this.mType = mType;
    }

    public String getTitle() {
        switch (mType) {
            case GOOD_NEWS:
                return mContext.getResources().getString(R.string.info_tip_title_good_news);
            case OH_NO:
                return mContext.getResources().getString(R.string.info_tip_title_oh_no);
            case SENSITIVE_BREACHES:
                return mContext.getResources().getString(R.string.info_tip_title_sensitive_breaches);
            case RETIRED_BREACHES:
                return mContext.getResources().getString(R.string.info_tip_title_retired_breaches);
            default:
                return "";
        }
    }

    public String getDescription() {
        switch (mType) {
            case GOOD_NEWS:
                return mContext.getResources().getString(R.string.info_tip_description_good_news);
            case OH_NO:
                return String.format(mContext.getResources().getString(R.string.info_tip_description_oh_no), mTotalBreaches);
            case SENSITIVE_BREACHES:
                return mContext.getResources().getString(R.string.info_tip_description_sensitive_breaches);
            case RETIRED_BREACHES:
                return mContext.getResources().getString(R.string.info_tip_description_retired_breaches);
            default:
                return "";
        }
    }

    public String getTitleButton() {
        switch (mType) {
            case SENSITIVE_BREACHES:
                return mContext.getResources().getString(R.string.info_tip_show_me);
            case RETIRED_BREACHES:
                return mContext.getResources().getString(R.string.info_tip_show_me);
            default:
                return "";
        }
    }

    public int getIcon() {
        switch (mType) {
            case GOOD_NEWS:
                return R.drawable.info_tip_good_news;
            case OH_NO:
                return R.drawable.info_tip_oh_no;
            case SENSITIVE_BREACHES:
                return R.drawable.info_tip_sensitive;
            case RETIRED_BREACHES:
                return R.drawable.info_tip_retired;
            default:
                return 0;
        }
    }

    public int getColor() {
        switch (mType) {
            case GOOD_NEWS:
                return R.color.colorPrimaryDarkGreen;
            case OH_NO:
                return R.color.colorPrimaryDarkRed;
            case SENSITIVE_BREACHES:
                return R.color.colorPrimaryDarkAmber;
            case RETIRED_BREACHES:
                return R.color.colorPrimaryBlue;
            default:
                return 0;
        }
    }
}
