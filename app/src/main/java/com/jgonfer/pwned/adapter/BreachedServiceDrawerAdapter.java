package com.jgonfer.pwned.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jgonfer.pwned.Constants;
import com.jgonfer.pwned.R;
import com.jgonfer.pwned.model.BreachedServiceDrawerItem;
import com.jgonfer.pwned.model.InfoTip;
import com.jgonfer.pwned.model.InfoTipDrawerItem;
import com.jgonfer.pwned.model.realm.BreachedService;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by jgonfer on 12/6/16.
 */

public class BreachedServiceDrawerAdapter extends RecyclerView.Adapter<BreachedServiceDrawerAdapter.MyViewHolder> {

    private static final int ITEM_VIEW_TYPE_INFO_TIP = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    // Declare objects
    List<BreachedServiceDrawerItem> mData = Collections.emptyList();
    List<InfoTipDrawerItem> mInfoData = Collections.emptyList();
    private LayoutInflater mInflater;
    private Context mContext;
    private InfoTip mType;

    public BreachedServiceDrawerAdapter(Context context, List<BreachedServiceDrawerItem> data, List<InfoTipDrawerItem> infoData, InfoTip type) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mInfoData = infoData;
        this.mType = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_INFO_TIP) {
            View view = mInflater.inflate(R.layout.info_tip_draw_row, parent, false);
            return new MyViewHolder(view, viewType);
        } else {
            View view = mInflater.inflate(R.layout.breached_service_draw_row, parent, false);
            return new MyViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (isInfoTip(position)) {
            int infoPosition = 0;
            if (position > 0) {
                infoPosition = position - mData.size();
            }
            InfoTipDrawerItem current = mInfoData.get(infoPosition);
            int color = current.getColor();
            int icon = current.getIcon();
            holder.mInfoTipIcon.setImageDrawable(mContext.getResources().getDrawable(icon));
            holder.mInfoTipIcon.setColorFilter(mContext.getResources().getColor(color));
            holder.mInfoTipTitle.setText(current.getTitle());
            holder.mInfoTipTitle.setTextColor(mContext.getResources().getColor(color));
            holder.mInfoTipDescription.setText(current.getDescription());
            String showMe = current.getTitleButton();
            if (!showMe.isEmpty()) {
                holder.mInfoTipShowMe.setText(showMe);
                holder.mInfoTipShowMe.setVisibility(View.VISIBLE);
            } else {
                holder.mInfoTipShowMe.setVisibility(View.GONE);
            }
            holder.mInfoTipShowMe.setTextColor(mContext.getResources().getColor(color));
        } else {
            BreachedServiceDrawerItem current = mData.get(position - 1);
            BreachedService breachedService = current.getBreachedService();
            holder.mBreachedServiceTitle.setText(breachedService.getTitle());

            String baseUrl = Constants.kBaseUrlHIBPLogoImages;
            String title = breachedService.getTitle();
            String logoType = breachedService.getLogoType();
            if (logoType.equals("svg")) {
                logoType = "png";
                baseUrl = Constants.kBaseUrlJGonFerLogoImages;
            }
            if (current.isFixed()) {
                holder.mBreachedServiceStatus.setBackgroundColor(Color.GREEN);
            } else {
                holder.mBreachedServiceStatus.setBackgroundColor(Color.RED);
            }
            String url = baseUrl + title + "." + logoType;
            //Picasso.with(mContext).load(url).placeholder(R.drawable.default_image).into(holder.mBreachedServiceLogo);
            Picasso.with(mContext).load(url).into(holder.mBreachedServiceLogo);

            holder.mTopBarStatusRanking.setVisibility(View.GONE);
            String counter = NumberFormat.getNumberInstance(Locale.US).format(breachedService.getPwnCount());
            holder.mBreachedServiceCounter.setText(counter);
            switch (current.getPosition()) {
                case 0:
                    break;
                case 1:
                    holder.mTopBarStatusRanking.setVisibility(View.VISIBLE);
                    holder.mTopBarStatusRanking.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDarkAmber));
                    holder.mTopBarTrophyLabel.setText("1st");
                    break;
                case 2:
                    holder.mTopBarStatusRanking.setVisibility(View.VISIBLE);
                    holder.mTopBarStatusRanking.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryBlueGrey));
                    holder.mTopBarTrophyLabel.setText("2nd");
                    break;
                case 3:
                    holder.mTopBarStatusRanking.setVisibility(View.VISIBLE);
                    holder.mTopBarStatusRanking.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryBrown));
                    holder.mTopBarTrophyLabel.setText("3rd");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isInfoTip(position) ?
                ITEM_VIEW_TYPE_INFO_TIP : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isInfoTip(int position) {
        if (mType == InfoTip.NONE) {
            return false;
        }

        if (position == 0) {
            return true;
        }

        return position > mData.size();
    }

    @Override
    public int getItemCount() {
        return mData.size() + mInfoData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mBreachedServiceTitle;
        TextView mBreachedServiceCounter;
        ImageView mBreachedServiceLogo;
        ImageView mBreachedServiceStatus;
        LinearLayout mTopBarStatusRanking;
        ImageView mTopBarTrophyIcon;
        TextView mTopBarTrophyLabel;

        ImageView mInfoTipIcon;
        TextView mInfoTipTitle;
        TextView mInfoTipDescription;
        TextView mInfoTipShowMe;

        public MyViewHolder(View itemView, int type) {
            super(itemView);

            if (type == ITEM_VIEW_TYPE_INFO_TIP) {
                mInfoTipIcon = (ImageView) itemView.findViewById(R.id.info_trip_icon);
                mInfoTipTitle = (TextView) itemView.findViewById(R.id.info_trip_title);
                mInfoTipDescription = (TextView) itemView.findViewById(R.id.info_trip_description);
                mInfoTipShowMe = (TextView) itemView.findViewById(R.id.info_trip_show_me);
            } else {
                mBreachedServiceTitle = (TextView) itemView.findViewById(R.id.breached_service_title);
                mBreachedServiceCounter = (TextView) itemView.findViewById(R.id.breached_service_counter);
                mBreachedServiceLogo = (ImageView) itemView.findViewById(R.id.breached_service_logo);
                mBreachedServiceStatus = (ImageView) itemView.findViewById(R.id.breached_service_status);
                mTopBarStatusRanking = (LinearLayout) itemView.findViewById(R.id.top_bar_status_ranking);
                mTopBarTrophyIcon = (ImageView) itemView.findViewById(R.id.top_bar_trophy_icon);
                mTopBarTrophyLabel = (TextView) itemView.findViewById(R.id.top_bar_trophy_label);
            }
        }
    }
}