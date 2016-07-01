/**
 * Created by jgonzalez on 17/6/16.
 */

package com.jgonfer.pwned.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jgonfer.pwned.R;
import com.jgonfer.pwned.activity.MainActivity;
import com.jgonfer.pwned.adapter.BreachedServiceDrawerAdapter;
import com.jgonfer.pwned.connection.requests.AllBreachedServicesListRequest;
import com.jgonfer.pwned.model.BreachedServiceDrawerItem;
import com.jgonfer.pwned.model.InfoTip;
import com.jgonfer.pwned.model.InfoTipDrawerItem;
import com.jgonfer.pwned.model.realm.BreachedService;
import com.jgonfer.pwned.utils.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RankingFragment extends BaseFragment implements AllBreachedServicesListRequest.OnLoginResponseListener {
    Unbinder unbinder;

    @BindView(R.id.drawerList) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

    AllBreachedServicesListRequest mAllBreachedServiceListRequest;

    private Context mContext;
    private static BreachedService[] mBreachedServices;
    private BreachedServiceDrawerAdapter mAdapter;

    public RankingFragment() {

    }

    public void setAllBreachedServiceListRequest(AllBreachedServicesListRequest mAllBreachedServiceListRequest) {
        this.mAllBreachedServiceListRequest = mAllBreachedServiceListRequest;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        mAdapter = new BreachedServiceDrawerAdapter(mContext, getData(), getInfoData(), getInfoTipType());
        if (mAdapter != null && mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, mRecyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //mDrawerListener.onDrawerItemSelected(view, position);
                    if (mBreachedServices.length > position) {
                        int counter = 0;
                        BreachedService breachedService = mBreachedServices[position];
                        /*
                        if (shouldLoadFavoriteStations) {
                            for (BikeStation stationListed : RealmHelper.getBikeStationsForCurrentBikeService(mContext, false)) {
                                if (station.getIdStation().equals(stationListed.getIdStation()))
                                    position = counter;
                                counter++;
                            }
                        }
                        if (mDrawerListener != null)
                            mDrawerListener.onDrawerItemSelectedBikeStation(position);
                        */
                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadBreachedServices();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        downloadBreachedServices();

        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        refreshListViewData(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        mSwipeRefreshLayout.removeAllViews();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }

    public void downloadBreachedServices() {
        if (mAllBreachedServiceListRequest == null) {
            mAllBreachedServiceListRequest = new AllBreachedServicesListRequest(this);
        } else if (mAllBreachedServiceListRequest.getBreachedServiceListRequestCallback() == null) {
            mAllBreachedServiceListRequest.setBreachedServiceListRequestCallback(this);
        }
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mAllBreachedServiceListRequest.getAllBreachedServices(this);
    }

    public List<BreachedServiceDrawerItem> getData() {
        List<BreachedServiceDrawerItem> data = new ArrayList<>();

        // Set Navigation drawer items

        if (mBreachedServices != null) {
            int counter = 1;
            for (BreachedService breachedService : mBreachedServices) {
                BreachedServiceDrawerItem coItem = new BreachedServiceDrawerItem(breachedService, false, counter);
                data.add(coItem);
                counter++;
            }
        }

        return data;
    }

    public List<InfoTipDrawerItem> getInfoData() {
        List<InfoTipDrawerItem> data = new ArrayList<>();

        if (mBreachedServices != null) {
            if (mBreachedServices.length <= 0) {
                InfoTipDrawerItem gnItem = new InfoTipDrawerItem(InfoTip.GOOD_NEWS, getActivity(), mBreachedServices.length);
                data.add(gnItem);
            } else {
                InfoTipDrawerItem ohItem = new InfoTipDrawerItem(InfoTip.OH_NO, getActivity(), mBreachedServices.length);
                data.add(ohItem);
            }
            InfoTipDrawerItem sbItem = new InfoTipDrawerItem(InfoTip.SENSITIVE_BREACHES, getActivity(), mBreachedServices.length);
            data.add(sbItem);
            InfoTipDrawerItem rbItem = new InfoTipDrawerItem(InfoTip.RETIRED_BREACHES, getActivity(), mBreachedServices.length);
            data.add(rbItem);
        }

        return data;
    }

    public InfoTip getInfoTipType() {
        if (mBreachedServices != null) {
            if (mBreachedServices.length <= 0) {
                return InfoTip.GOOD_NEWS;
            } else {
                return InfoTip.OH_NO;
            }
        } else {
            return InfoTip.NONE;
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private static final String TAG = RecyclerTouchListener.class.getSimpleName();
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.d(TAG, "onSingleTapUp");
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    Log.d(TAG, "onLongPress");
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void refreshListViewData(boolean shouldReload) {
        mBreachedServices = RealmHelper.getBreachedServicesOrdered();
        mAdapter = new BreachedServiceDrawerAdapter(mContext, getData(), getInfoData(), getInfoTipType());
        mAdapter.notifyDataSetChanged();
        if (shouldReload) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void refreshListViewWithoutData(boolean shouldReload) {
        mBreachedServices = null;
        mAdapter = new BreachedServiceDrawerAdapter(mContext, getData(), getInfoData(), getInfoTipType());
        mAdapter.notifyDataSetChanged();
        if (shouldReload) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onAllBreachedServicesListResponse() {
        Log.d(MainActivity.class.getSimpleName(), "onAllBreachedServicesListResponse()");

        mSwipeRefreshLayout.setRefreshing(false);
        refreshListViewData(true);
    }

    @Override
    public void onAllBreachedServicesListErrorResponse(String errorMessage) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d(MainActivity.class.getSimpleName(), "onAllBreachedServicesListErrorResponse(): " + errorMessage);
    }
}
