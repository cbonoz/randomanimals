package com.randomanimals.www.randomanimals.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.randomanimals.www.randomanimals.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment {

    public ShareFragment() {
        // Required empty public constructor
    }

//    https://www.tutorialspoint.com/android/android_twitter_integration.htm

    private void shareTwitter(String message) {
        Intent tweet = new Intent(Intent.ACTION_VIEW);
        tweet.setData(Uri.parse("http://twitter.com/?status=" + Uri.encode(message)));
        startActivity(tweet);
    }

    ImageButton twitterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        twitterButton = (ImageButton) view.findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String shareString = getString(R.string.twitter_line);
                shareTwitter(shareString);
            }
        });

        return view;
    }

}
