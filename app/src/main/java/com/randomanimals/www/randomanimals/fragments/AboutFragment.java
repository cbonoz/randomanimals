package com.randomanimals.www.randomanimals.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.randomanimals.www.randomanimals.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.theWebView) WebView view;

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
