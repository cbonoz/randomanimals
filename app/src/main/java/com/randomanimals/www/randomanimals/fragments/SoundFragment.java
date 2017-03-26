package com.randomanimals.www.randomanimals.fragments;


import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poliveira.parallaxrecyclerview.HeaderLayoutManagerFixed;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.models.SoundFile;

import java.util.ArrayList;
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

    public SoundFragment() {
        // Required empty public constructor
    }

    private List<SoundFile> soundFiles;

    private void createAdapter(RecyclerView recyclerView) {
        final List<SoundFile> content = new ArrayList<>();
        soundFiles = ((MainActivity) getActivity()).getSoundFiles();
        content.addAll(soundFiles);

        final ParallaxRecyclerAdapter<SoundFile> adapter = new ParallaxRecyclerAdapter<SoundFile>(content) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<SoundFile> adapter, int i) {
                ((SoundFragment.ViewHolder) viewHolder).textView.setText(adapter.getData().get(i).getAnimal());
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<SoundFile> adapter, int i) {
                return new SoundFragment.ViewHolder(getActivity().getLayoutInflater().inflate(R.layout.sound_list_item, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<SoundFile> adapter) {
                return content.size();
            }
        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                ((MainActivity) getActivity()).launchSoundFragment(soundFiles.get(position));
//                playSoundAtPosition(position);
            }
        });


        HeaderLayoutManagerFixed layoutManagerFixed = new HeaderLayoutManagerFixed(getActivity());
        recyclerView.setLayoutManager(layoutManagerFixed);
        View header = getActivity().getLayoutInflater().inflate(R.layout.header, recyclerView, false);
        layoutManagerFixed.setHeaderIncrementFixer(header);
        adapter.setShouldClipView(false);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        createAdapter(mRecyclerView);
        return view;
    }

}
