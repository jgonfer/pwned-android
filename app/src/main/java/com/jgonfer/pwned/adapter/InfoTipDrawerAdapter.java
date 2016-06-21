package com.jgonfer.pwned.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgonfer.pwned.R;
import com.jgonfer.pwned.model.InfoTipDrawerItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by jgonfer on 12/6/16.
 */

public class InfoTipDrawerAdapter extends RecyclerView.Adapter<InfoTipDrawerAdapter.MyViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    // Declare objects
    List<InfoTipDrawerItem> mData = Collections.emptyList();
    private LayoutInflater mInflater;
    private Context mContext;

    public InfoTipDrawerAdapter(Context context, List<InfoTipDrawerItem> data) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.info_tip_draw_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (isHeader(position)) return;

        InfoTipDrawerItem current = mData.get(position);
        int color = current.getColor();
        int icon = current.getIcon();
        holder.mInfoTipIcon.setImageDrawable(mContext.getResources().getDrawable(icon));
        holder.mInfoTipIcon.setColorFilter(color);
        holder.mInfoTipTitle.setText(current.getTitle());
        //holder.mInfoTipDescription.setText(current.getDescription());
        String showMe = current.getTitleButton();
        if (!showMe.isEmpty()) {
            holder.mInfoTipShowMe.setText(showMe);
            holder.mInfoTipShowMe.setVisibility(View.VISIBLE);
        } else {
            holder.mInfoTipShowMe.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ?
                ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        /*
        if (position == mData.size() - 1) {
            return true;
        } else {
            return false;
        }
        */
        return false;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mInfoTipIcon;
        TextView mInfoTipTitle;
        TextView mInfoTipDescription;
        TextView mInfoTipShowMe;

        public MyViewHolder(View itemView) {
            super(itemView);

            mInfoTipIcon = (ImageView) itemView.findViewById(R.id.info_trip_icon);
            mInfoTipTitle = (TextView) itemView.findViewById(R.id.info_trip_title);
            mInfoTipDescription = (TextView) itemView.findViewById(R.id.info_trip_description);
            mInfoTipShowMe = (TextView) itemView.findViewById(R.id.info_trip_show_me);
        }
    }
}