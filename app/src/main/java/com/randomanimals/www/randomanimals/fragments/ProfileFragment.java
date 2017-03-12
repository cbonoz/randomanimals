package com.randomanimals.www.randomanimals.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.events.ProfileEvent;
import com.randomanimals.www.randomanimals.models.UserInfo;
import com.randomanimals.www.randomanimals.services.WebService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    public ProfileFragment() {
        // Required empty public constructor
    }

    private String androidId;

    TextView userPlaysText;
    TextView userNameText;
    Button changeUserButton;

    public void showUserDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new UserDialogFragment();
        dialog.show(getActivity().getSupportFragmentManager(), "UserDialogFragment");
    }

    private void getProfile() {
        Intent profileIntent = new Intent(getActivity(), WebService.class);
        try {
            JSONObject profileJson = new JSONObject();
            profileJson.put("userid", androidId);

            profileIntent.putExtra("url", Constants.PROFILE_URL);
            profileIntent.putExtra("body", profileJson.toString());
            ((MainActivity) getActivity()).startService(profileIntent);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity context = ((MainActivity) getActivity());
        androidId = context.getAndroidId();

        userPlaysText = (TextView) view.findViewById(R.id.userPlaysText);
        userPlaysText.setText(R.string.loading);

        userNameText = (TextView) view.findViewById(R.id.userNameText);
        userNameText.setText("Profile: " + UserInfo.username);

        changeUserButton = (Button) view.findViewById(R.id.changeUserButton);
        changeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserDialog();
                }
        });

        // Retrieve user information from server regarding play counts.
        getProfile();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProfileEvent(ProfileEvent event) {
        /* Do something when EventBus fires with this event */
        Log.d(TAG, "Received ProfileEvent: " + event.animals.size() + " animal entries");
        userPlaysText.setText(event.toString());
    };
}
