package com.randomanimals.www.randomanimals.fragments;


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.SoundActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SoundFragment with the list of animal sounds for the user to select from.
 */
public class SoundFragment extends Fragment {
    private static final String TAG = "SoundFragment";

    private boolean isNormalAdapter = false;
    private RecyclerView mRecyclerView;

    private final MediaPlayer player = new MediaPlayer();

    private AssetManager aMan;
    private String[] soundFiles;

    public SoundFragment() {
        // Required empty public constructor
    }

    private void setUpSoundList(View view) {
        try {
            aMan = getActivity().getAssets();
            soundFiles = aMan.list(Constants.SOUND_FOLDER);
        } catch (IOException e) {
            aMan = null;
            Log.e(TAG, e.toString());
            soundFiles =  new String[]{};
        }

        Log.d(TAG, "soundFiles: " + Arrays.toString(soundFiles));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        createAdapter(mRecyclerView);
    }

    private void createAdapter(RecyclerView recyclerView) {
        final List<String> content = new ArrayList<>();
        content.addAll(Arrays.asList(soundFiles));

        final ParallaxRecyclerAdapter<String> adapter = new ParallaxRecyclerAdapter<String>(content) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<String> adapter, int i) {
                ((SoundFragment.ViewHolder) viewHolder).textView.setText(adapter.getData().get(i));
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<String> adapter, int i) {
                return new SoundFragment.ViewHolder(getActivity().getLayoutInflater().inflate(R.layout.row_recyclerview, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<String> adapter) {
                return content.size();
            }
        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                ((MainActivity) getActivity()).launchSoundFragment(soundFiles[position]);
//                playSoundAtPosition(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View header = getActivity().getLayoutInflater().inflate(R.layout.header, recyclerView, false);
        adapter.setParallaxHeader(header, recyclerView);
        adapter.setData(content);
        recyclerView.setAdapter(adapter);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sound, container, false);
        setUpSoundList(view);
        return view;
    }

}
