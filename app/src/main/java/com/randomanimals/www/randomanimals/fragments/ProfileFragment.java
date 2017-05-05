package com.randomanimals.www.randomanimals.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.adapters.AnimalPlaysAdapter;
import com.randomanimals.www.randomanimals.events.ProfileEvent;
import com.randomanimals.www.randomanimals.events.UsernameEvent;
import com.randomanimals.www.randomanimals.fragments.dialogs.UserDialogFragment;
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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String androidId;

    TextView userPlaysText;
    TextView userNameText;
    Button changeUserButton;

    SpinKitView spinKitView;

    private void updateUserText(String username) {
        String text = String.format(getString(R.string.username_text), username);
        userNameText.setText(text);
    }

    public void showUserDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new UserDialogFragment();
        dialog.show(getActivity().getSupportFragmentManager(), "UserDialogFragment");
    }

    private void setupProfileList(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void getProfile() {
        spinKitView.setVisibility(View.VISIBLE);
        Intent profileIntent = new Intent(getActivity(), WebService.class);
        try {
            JSONObject profileJson = new JSONObject();
            profileJson.put("userId", androidId);

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


        spinKitView = (SpinKitView) view.findViewById(R.id.spinKit);
        userPlaysText = (TextView) view.findViewById(R.id.userPlaysText);
//        userPlaysText.setText(R.string.loading);

        setupProfileList(view);

        userNameText = (TextView) view.findViewById(R.id.userNameText);
        String text = String.format(getString(R.string.username_text),
                context.getStringPref(Constants.USERNAME_KEY));
        userNameText.setText(text);

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
        spinKitView.setVisibility(View.GONE);
        if (event.animals == null) {
            Toast.makeText(getActivity(), R.string.internet_error, Toast.LENGTH_LONG).show();
            userPlaysText.setVisibility(View.VISIBLE);
            userPlaysText.setText(R.string.internet_error);
            return;
        }
        final int numAnimals = event.animals.size();
        Log.d(TAG, "Received ProfileEvent: " + numAnimals + " animal entries");
        if (numAnimals > 0) {
//            userPlaysText.setText(event.toString());
            userPlaysText.setVisibility(View.GONE);
            mAdapter = new AnimalPlaysAdapter(event.animals);
            mRecyclerView.setAdapter(mAdapter);

            MainActivity context = (MainActivity) getActivity();
            String serverUsername = event.username;
            // If server and client username disagreement - use server.
            if (!context.username.equals(serverUsername)) {
                context.saveStringPref(Constants.USERNAME_KEY, serverUsername);
                updateUserText(serverUsername);
            }
        } else {
            userPlaysText.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(null);
            userPlaysText.setText(R.string.no_plays);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUsernameEvent(UsernameEvent event) {
        /* Do something when EventBus fires with this event */
        final String newUsername = event.username;
        final MainActivity context = (MainActivity) getActivity();
        if (newUsername == null) {
            Toast.makeText(context, R.string.internet_error, Toast.LENGTH_LONG).show();
            return;
        }
        context.saveStringPref(Constants.USERNAME_KEY, newUsername);
        updateUserText(newUsername);
        try {
            context.launchFragment(SoundListFragment.class.newInstance(), Constants.ENTER_LEFT);
            Toast.makeText(context, "User now: " + newUsername, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    };



}
