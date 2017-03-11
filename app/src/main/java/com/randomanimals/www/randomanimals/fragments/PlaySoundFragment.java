package com.randomanimals.www.randomanimals.fragments;


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaySoundFragment extends Fragment {
    private static final String TAG = "PlaySoundFragment";

    private AssetManager aMan;
    MediaPlayer player;
    Button buttonPlayPause;
    Button backButton;
    ImageView Image;
    SeekBar seekBar;
    Handler handler;
    TextView titleText;

    String soundTitle;
    String soundFile;

    public PlaySoundFragment() {
        // Required empty public constructor

    }

    private void playSoundFile() {
        final String clickText = "Sound: " + soundFile;
        Log.d(TAG, "Playing " + clickText);
        Toast.makeText(getActivity(), clickText, Toast.LENGTH_SHORT).show();
        String joinedPath = new File(Constants.SOUND_FOLDER, soundFile).toString();
        try {
            AssetFileDescriptor afd = aMan.openFd(joinedPath);
            player.setDataSource(afd.getFileDescriptor());
            player.prepare();
            player.start();
            buttonPlayPause.setBackgroundResource(android.R.drawable.ic_media_pause);
            stateMediaPlayer = stateMP_Playing;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

//    http://stackoverflow.com/questions/16141167/android-audio-seekbar

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_sound, container, false);

//        Image = (ImageView) view.findViewById(R.id.pdfview);
//        Image.setImageResource(R.drawable.wereim);

        final Bundle bundle = getArguments();
        soundTitle = bundle.getString("soundTitle");
        soundFile = bundle.getString("soundFile");

        titleText = (TextView) view.findViewById(R.id.titleText);
        titleText.setText(soundTitle);

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarOnSeekChangeListener);

        buttonPlayPause = (Button) view.findViewById(R.id.playPauseButton);
        buttonPlayPause.setOnClickListener(buttonPlayPauseOnClickListener);

        backButton = (Button)  view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((MainActivity) getActivity()).launchFragment(
                            SoundFragment.class.newInstance(), Constants.ENTER_LEFT);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        aMan = getActivity().getAssets();
        initMediaPlayer();
        playSoundFile();
        return view;
    }



    private int stateMediaPlayer;
    private final int stateMP_NotStarter = 0;
    private final int stateMP_Playing = 1;
    private final int stateMP_Pausing = 2;
    private int mediaPos;
    private int mediaMax;


    private void initMediaPlayer() {
        handler = new Handler();
        player = new MediaPlayer();
//        player = MediaPlayer.create(were.this, R.raw.were);
        stateMediaPlayer = stateMP_NotStarter;
        mediaPos = player.getCurrentPosition();

        mediaMax = player.getDuration();

        seekBar.setMax(mediaMax); // Set the Maximum range of the
        // seekBar.setProgress(mediaPos);// set
        // current progress to song's

        handler.removeCallbacks(moveSeekBarThread);
        handler.postDelayed(moveSeekBarThread, 100);
    }

    private Runnable moveSeekBarThread = new Runnable() {

        public void run() {
            if (player.isPlaying()) {

                int mediaPos_new = player.getCurrentPosition();
                int mediaMax_new = player.getDuration();
                seekBar.setMax(mediaMax_new);
                seekBar.setProgress(mediaPos_new);

                handler.postDelayed(this, 100); // Looping the thread after 0.1
                // second
                // seconds
            }
        }
    };

    Button.OnClickListener buttonPlayPauseOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (stateMediaPlayer) {
                case stateMP_NotStarter:
                    playSoundFile();
//                    player.start();
//                    buttonPlayPause.setBackgroundResource(android.R.drawable.ic_media_pause);
//                    stateMediaPlayer = stateMP_Playing;
                    break;
                case stateMP_Playing:
                    player.pause();
                    buttonPlayPause.setBackgroundResource(android.R.drawable.ic_media_play);
                    stateMediaPlayer = stateMP_Pausing;
                    break;
                case stateMP_Pausing:
                    player.start();
                    buttonPlayPause.setBackgroundResource(android.R.drawable.ic_media_pause);
                    stateMediaPlayer = stateMP_Playing;
                    break;
            }

        }
    };

    SeekBar.OnSeekBarChangeListener seekBarOnSeekChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser) {
                player.seekTo(progress);
                seekBar.setProgress(progress);
            }

        }
    };

}
