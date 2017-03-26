package com.randomanimals.www.randomanimals.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.adapters.UserPlaysAdapter;
import com.randomanimals.www.randomanimals.events.LeaderEvent;
import com.randomanimals.www.randomanimals.models.SoundFile;
import com.randomanimals.www.randomanimals.services.WebService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "LeaderFragment";

    public LeaderFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Spinner animalSpinner;

    TextView leaderBoardText;
    SpinKitView spinKitView;

    private void getLeaderBoardForCurrentAnimal(String currentAnimal) {
        Log.d(TAG , "currentAnimal: " + currentAnimal);
        spinKitView.setVisibility(View.VISIBLE);
        Intent leaderIntent = new Intent(getActivity(), WebService.class);
        try {
            JSONObject leaderJson = new JSONObject();
            leaderJson.put("animal", currentAnimal);
            leaderIntent.putExtra("url", Constants.ANIMAL_URL);
            leaderIntent.putExtra("body", leaderJson.toString());
            ((MainActivity) getActivity()).startService(leaderIntent);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void setUpAnimalSpinnerSelector(View view) throws Exception {
        final MainActivity context = (MainActivity) getActivity();
        animalSpinner = (Spinner) view.findViewById(R.id.animalSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<String> spinnerArray = new ArrayList<>();
        for (SoundFile soundFile : context.getSoundFiles()) {
            spinnerArray.add(soundFile.getAnimal());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner. q
        animalSpinner.setAdapter(adapter);
        animalSpinner.setOnItemSelectedListener(this);
        // Attempt to select the leaderboard for the first animal

        SharedPreferences sharedPref = context.getPreferences(MODE_PRIVATE);
        int position = sharedPref.getInt(Constants.SPINNER_KEY,-1);
        if(position < 0 || position >= spinnerArray.size()) {
            position = 0;
        }
        animalSpinner.setSelection(position);
//        getLeaderBoardForCurrentAnimal(adapter.getItem(position));
    }

    private void setUpLeaderBoardList(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.leader_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader, container, false);

        spinKitView = (SpinKitView) view.findViewById(R.id.spinKit);
        leaderBoardText = (TextView) view.findViewById(R.id.leaderBoardText);
//        leaderBoardText.setText(R.string.loading);



        try {
            setUpLeaderBoardList(view);
            setUpAnimalSpinnerSelector(view);
        } catch( Exception e) {
            Log.e(TAG, e.toString());
        }

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
    public void onLeaderEvent(LeaderEvent event) {
        // Clear the loading spinner and populate the view with the result list.
        spinKitView.setVisibility(View.GONE);
        if (event.animals == null) {
            Toast.makeText(getActivity(), R.string.internet_error, Toast.LENGTH_LONG).show();
            leaderBoardText.setVisibility(View.VISIBLE);
            leaderBoardText.setText(R.string.internet_error);
            return;
        }

        final int numLeaders = event.animals.size();
        Log.d(TAG, "Received LeaderEvent: " + numLeaders + " animal entries");
        if (numLeaders > 0) {
            leaderBoardText.setVisibility(View.INVISIBLE);
            // Populate the leaderBoardAdapter
//            leaderBoardText.setText(event.toString());
            // specify an adapter (see also next example)
            mAdapter = new UserPlaysAdapter(event.animals);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            leaderBoardText.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(null);
            leaderBoardText.setText(R.string.no_leaders);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        Log.d(TAG, "onItemSelected");
        try {
            MainActivity context = (MainActivity)  getActivity();
            SharedPreferences sharedPref = context.getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.putInt(Constants.SPINNER_KEY, pos);
            prefEditor.apply();
            String currentAnimal = (String) parent.getItemAtPosition(pos);
            Log.d(TAG, "Selected: " + currentAnimal);
            getLeaderBoardForCurrentAnimal(currentAnimal);
        } catch (Exception e) {
            Log.e(TAG, "onItemSelected error: " + e.toString());
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        Log.d(TAG, "onNothingSelected");
    }
}
//
//
//    private boolean connectSocket() {
//        try {
//            socket = IO.socket(Constants.IO_SOCKET);
//        } catch (URISyntaxException e) {
//            Log.e(TAG, e.toString());
//            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//
//            @Override
//            public void call(Object... args) {
//                socket.emit("foo", "hi");
//                socket.disconnect();
//                Log.d(TAG, "Connect");
//            }
//
//        }).on(Constants.PLAY_EVENT, new Emitter.Listener() {
//
//            @Override
//            public void call(Object... args) {
//                Log.d(TAG, "Play Event recorded");
//                Log.d(TAG, "args: " + args.toString());
//            }
//
//        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//
//            @Override
//            public void call(Object... args) {
//                Log.d(TAG, "Disconnect");
//            }
//
//        });
//        socket.connect();
//
//        return true;
//    }
//

