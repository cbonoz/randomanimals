package com.randomanimals.www.randomanimals.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.poliveira.parallaxrecyclerview.HeaderLayoutManagerFixed;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.fragments.dialogs.RandomDialog;
import com.randomanimals.www.randomanimals.models.SoundFile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SoundListFragment with the list of animal sounds for the user to select from.
 */
public class SoundListFragment extends Fragment {
    private static final String TAG = "SoundListFragment";

    private static final int STANDARD_BONUS = 1;

    @BindView(R.id.recycler) RecyclerView mRecyclerView;

    private ImageButton headerButton;

    public SoundListFragment() {
        // Required empty public constructor
    }

    private List<SoundFile> soundList;
    private static int firstVisiblePosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundList = ((MainActivity) getActivity()).getSoundFiles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_sound, container, false);
        ButterKnife.bind(this, view);

        initSoundListAdapter(mRecyclerView);
        return view;
    }

    private void saveListPosition() {
        // save index and top position
        firstVisiblePosition = ((HeaderLayoutManagerFixed) mRecyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();
    }

    private void restoreListPosition() {
        mRecyclerView.getLayoutManager().scrollToPosition(firstVisiblePosition);
    }

    // ** List View Adapter Logic Below ** //

    private void initSoundListAdapter(RecyclerView recyclerView) {
        final ParallaxRecyclerAdapter<SoundFile> adapter = new ParallaxRecyclerAdapter<SoundFile>(soundList) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<SoundFile> adapter, int i) {
                ((SoundListFragment.ViewHolder) viewHolder).textView.setText(adapter.getData().get(i).animal);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<SoundFile> adapter, int i) {
                return new SoundListFragment.ViewHolder(getActivity().getLayoutInflater().inflate(R.layout.sound_list_item, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<SoundFile> adapter) {
                return soundList.size();
            }
        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                ((MainActivity) getActivity()).launchSoundFragment(
                        soundList.get(position),
                        position,
                        STANDARD_BONUS);
            }
        });

        HeaderLayoutManagerFixed layoutManagerFixed = new HeaderLayoutManagerFixed(getActivity());
        recyclerView.setLayoutManager(layoutManagerFixed);
        View header = getActivity().getLayoutInflater().inflate(R.layout.header, recyclerView, false);

        headerButton = (ImageButton) header.findViewById(R.id.headerImageButton);
        headerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onHeaderButton click");
                DialogFragment newFragment = RandomDialog.newInstance(10);
                newFragment.show(getActivity().getSupportFragmentManager(), "Random Animal");
            }
        });

        layoutManagerFixed.setHeaderIncrementFixer(header);
        adapter.setShouldClipView(false);
        adapter.setParallaxHeader(header, recyclerView);
        adapter.setData(soundList);
        recyclerView.setAdapter(adapter);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreListPosition();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveListPosition();
    }
}
