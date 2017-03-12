package com.randomanimals.www.randomanimals.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.events.LeaderEvent;
import com.randomanimals.www.randomanimals.events.ProfileEvent;
import com.randomanimals.www.randomanimals.services.WebService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderFragment extends Fragment {
    private static final String TAG = "LeaderFragment";

    public LeaderFragment() {
        // Required empty public constructor
    }

    private static Socket socket;
    private String currentAnimal = "doggo";

    TextView leaderBoardText;

    private void getLeaderBoard() {
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

    public void setCurrentAnimal(String animal) {
        currentAnimal = animal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader, container, false);

        leaderBoardText = (TextView) view.findViewById(R.id.leaderBoardText);
        leaderBoardText.setText(R.string.loading);

        getLeaderBoard();
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
        /* Do something when EventBus fires with this event */
        Log.d(TAG, "Received LeaderEvent: " + event.animals.size() + " animal entries");
        leaderBoardText.setText(event.toString());
    };

    private boolean connectSocket() {

        try {
            socket = IO.socket(Constants.IO_SOCKET);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socket.emit("foo", "hi");
                socket.disconnect();
                Log.d(TAG, "Connect");
            }

        }).on(Constants.PLAY_EVENT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d(TAG, "Play Event recorded");
                Log.d(TAG, "args: " + args.toString());
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d(TAG, "Disconnect");
            }

        });
        socket.connect();

        return true;
    }
}
