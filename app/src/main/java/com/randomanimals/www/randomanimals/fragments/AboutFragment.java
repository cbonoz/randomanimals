package com.randomanimals.www.randomanimals.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private static final String TAG = AboutFragment.class.getSimpleName();

    public AboutFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.theWebView) WebView view;
    @OnClick(R.id.backButton) void onBackClick() {
        MainActivity activity = (MainActivity) getActivity();
        Fragment fragment;
        try {
            fragment = SoundListFragment.class.newInstance();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            fragment = null;
        }
        if (fragment != null) {
            activity.launchFragment(fragment, Constants.ENTER_LEFT);
        } else {
            Log.e(TAG, "backButton: fragment was null") ;
            Toast.makeText(activity, "Use Navigation Side Menu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        String url = getString(R.string.about_file);
        loadWebView(url);
        return view;
    }

    /**
     * general helper function to load the given url in the webview on the activity layout
     * @param url
     */
    private void loadWebView(String url) {
        view.loadUrl(url);
    }

}
