/**
 * Created by jgonzalez on 17/6/16.
 */

package com.jgonfer.pwned.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jgonfer.pwned.R;


public class PasswordFragment extends Fragment {
    public PasswordFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_password, container, false);

        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        //((MainActivity) getActivity()).sendScreenName("About");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
