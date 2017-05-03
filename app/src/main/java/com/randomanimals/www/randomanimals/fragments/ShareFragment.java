package com.randomanimals.www.randomanimals.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment {
    private static final String TAG = "ShareFragment";

    public ShareFragment() {
        // Required empty public constructor
    }

//    https://www.tutorialspoint.com/android/android_twitter_integration.htm
    private void shareTwitter(String message) {
        Intent tweet = new Intent(Intent.ACTION_VIEW);
        tweet.setData(Uri.parse("http://twitter.com/intent/tweet?text=" + Uri.encode(message)));
        startActivity(tweet);
    }

    ImageButton twitterButton;
    ImageButton shareButton;

    @OnClick(R.id.twitterButton) void onTwitterClick() {
        final String shareString = getString(R.string.twitter_line);
        shareTwitter(shareString);
    }

    @OnClick(R.id.textMessageButton) void onTextMessageClick() {
        Log.d(TAG, "onClick");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        final String animalUri = "PLAY_STORE_URL";
        sendIntent.putExtra("sms_body", "Free animal sounds app on android, pretty funny. " +
                animalUri);
        startActivity(sendIntent);
        Toast.makeText(getActivity(), "select text application", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}
