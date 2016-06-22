/**
 * Created by jgonzalez on 17/6/16.
 */

package com.jgonfer.pwned.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jgonfer.pwned.R;
import com.jgonfer.pwned.activity.MainActivity;
import com.jgonfer.pwned.adapter.BreachedServiceDrawerAdapter;
import com.jgonfer.pwned.connection.requests.BreachedServicesListRequest;
import com.jgonfer.pwned.model.BreachedServiceDrawerItem;
import com.jgonfer.pwned.model.InfoTip;
import com.jgonfer.pwned.model.InfoTipDrawerItem;
import com.jgonfer.pwned.model.realm.BreachedService;
import com.jgonfer.pwned.utils.RealmHelper;
import com.jgonfer.pwned.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SearchFragment extends BaseFragment implements BreachedServicesListRequest.OnLoginResponseListener {

    @BindView(R.id.edit_text_username) EditText mSearchEditText;
    @BindView(R.id.search_tool_clear) TextView mToolClear;
    @BindView(R.id.search_tool_gmail) TextView mToolGmail;
    @BindView(R.id.search_tool_yahoo) TextView mToolYahoo;
    @BindView(R.id.search_tool_aol) TextView mToolAol;
    @BindView(R.id.search_tool_hotmail) TextView mToolHotmail;
    @BindView(R.id.search_tool_gmx) TextView mToolGmx;
    @BindView(R.id.drawerList) RecyclerView mRecyclerView;
    @BindView(R.id.search_button_username_email) ImageButton mSearchButton;
    @BindView(R.id.search_toolbox) LinearLayout mSearchToolbox;
    @BindView(R.id.body_search_fragment) LinearLayout mBodySearchFragment;

    @BindDrawable(R.drawable.search_user) Drawable search_user_drawable;
    @BindDrawable(R.drawable.search_mail) Drawable search_email_drawable;

    private Animation slideDown, slideUp;

    private BreachedServicesListRequest mBreachedServiceListRequest;
    private Context mContext;
    private static BreachedService[] mBreachedServices;
    private BreachedServiceDrawerAdapter mAdapter;
    private String email = "", emailInEditText = "", TAG = SearchFragment.class.getSimpleName();
    private boolean isDownloaded = false;

    public SearchFragment() {

    }

    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manual, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, mRecyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //mDrawerListener.onDrawerItemSelected(view, position);
                    if (mBreachedServices.length > position) {
                        BreachedService breachedService = mBreachedServices[position];
                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQueryFromEditText();
            }
        });

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSearchToolbox.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isDownloaded) {
                    refreshListViewData(true);
                }
                mSearchToolbox.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean isEventActionValid = false;
                if (event != null) {
                    isEventActionValid = event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                }
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        isEventActionValid) {
                    //if (!event.isShiftPressed()) {
                        searchQueryFromEditText();
                        return true;
                    //}
                }
                return false;
            }
        });
        mSearchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean isEventActionValid = false;
                if (event != null) {
                    isEventActionValid = event.getAction() == KeyEvent.ACTION_DOWN;
                }
                if (keyCode == KeyEvent.KEYCODE_BACK ||
                        isEventActionValid) {
                    mSearchEditText.clearFocus();
                }
                return false;
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim().toLowerCase();
                if (Utils.isEmailValid(text)) {
                    mSearchButton.setImageDrawable(search_email_drawable);
                } else {
                    mSearchButton.setImageDrawable(search_user_drawable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSearchToolbox.startAnimation(slideDown);
                } else {
                    mSearchToolbox.startAnimation(slideUp);
                    hideKeyboard();
                }
            }
        });
        mSearchEditText.setText(emailInEditText);

        mToolClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
            }
        });

        mToolGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmailEndingIntoSearchEditText("@gmail.com");
            }
        });
        mToolYahoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmailEndingIntoSearchEditText("@yahoo.com");
            }
        });
        mToolAol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmailEndingIntoSearchEditText("@aol.com");
            }
        });
        mToolHotmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmailEndingIntoSearchEditText("@hotmail.com");
            }
        });
        mToolGmx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmailEndingIntoSearchEditText("@gmx.com");
            }
        });

        if (email != null && email.isEmpty()) {
            refreshListViewWithoutData(true);
        } else {
            refreshListViewData(true);
        }

        return rootView;
    }

    @OnClick(R.id.body_search_fragment)
    public void hideKeyboard() {
        Utils.hideKeyboard(mContext, mSearchEditText);
    }

    private void setEmailEndingIntoSearchEditText(String emailEnding) {
        String searchText = mSearchEditText.getText().toString();
        if (!searchText.contains(emailEnding)) {
            String[] words = searchText.split("\\@");
            searchText = words[0] + emailEnding;
        }
        mSearchEditText.setText(searchText);
        mSearchEditText.setSelection(searchText.length());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    public void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }

    public List<BreachedServiceDrawerItem> getData() {
        List<BreachedServiceDrawerItem> data = new ArrayList<>();

        if (mBreachedServices != null) {
            for (BreachedService breachedService : mBreachedServices) {
                if (breachedService != null) {
                    boolean entryIsFixed = RealmHelper.isFixedHistoryEntryByEmailAndBreachedService(email, breachedService.getName());
                    BreachedServiceDrawerItem coItem = new BreachedServiceDrawerItem(breachedService, entryIsFixed, 0);
                    data.add(coItem);
                }
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

    private void searchQueryFromEditText() {
        isDownloaded = false;
        mSearchEditText.clearFocus();
        email = getEmailFromSearchEditText();
        refreshListViewWithoutData(true);
        String query = getEmailFromSearchEditText();
        mBreachedServiceListRequest = new BreachedServicesListRequest(this);
        mBreachedServiceListRequest.getBreachedServices(this, query);
    }

    private void refreshListViewData(boolean shouldReload) {
        mBreachedServices = RealmHelper.getBreachedServicesByEmail(email);
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

    private String getEmailFromSearchEditText() {
        return mSearchEditText.getText().toString().trim().toLowerCase();
    }

    @Override
    public void onBreachedServicesListResponse() {
        Log.d(MainActivity.class.getSimpleName(), "onBreachedServicesListResponse()");
        isDownloaded = true;
        boolean isHidden = mSearchToolbox.getVisibility() == View.GONE;
        if (isHidden) {
            refreshListViewData(true);
        }
    }

    @Override
    public void onBreachedServicesListErrorResponse(String errorMessage) {
        Log.d(MainActivity.class.getSimpleName(), "onBreachedServicesListErrorResponse(): " + errorMessage);
        isDownloaded = true;
        boolean isHidden = mSearchToolbox.getVisibility() == View.GONE;
        if (isHidden) {
            refreshListViewData(true);
        }
    }
}
