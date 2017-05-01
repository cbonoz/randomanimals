package com.randomanimals.www.randomanimals.fragments;


import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.services.TimerUtil;
import com.randomanimals.www.randomanimals.services.WebService;

import org.json.JSONObject;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
 */
public class PlaySoundFragment extends Fragment implements
        SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "PlaySoundFragment";

    private AssetManager aMan;
    private static MediaPlayer player = null;
    ImageButton playPauseButton;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;

    Button backButton;
    SeekBar seekBar;
    Handler handler;
    TextView titleText;

    String animal;
    String fileName;

    private static final TimerUtil utils = new TimerUtil();

    public PlaySoundFragment() {
        // Required empty public constructor
    }

    private String androidId;

    private void incrementPlayCount() {
        Intent incIntent = new Intent(getActivity(), WebService.class);
        try {
            JSONObject incJson = new JSONObject();
            incJson.put("userId", androidId);
            incJson.put("animal", animal);
            MainActivity context = (MainActivity) getActivity();
            incJson.put("username", context.username);

            incIntent.putExtra("url", Constants.INCREMENT_URL);
            incIntent.putExtra("body", incJson.toString());
            ((MainActivity) getActivity()).startService(incIntent);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private SuperActivityToast.OnButtonClickListener onButtonClickListener = new SuperActivityToast.OnButtonClickListener() {
        @Override
        public void onClick(View view, Parcelable token) {
            try {
                handler.removeCallbacks(mUpdateTimeTask);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            MainActivity context = (MainActivity) getActivity();
            try {
                context.launchFragment(ProfileFragment.class.newInstance(), Constants.ENTER_LEFT);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                if (context != null) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void makeSuperToastOnIncrement(String text) {
        SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_BUTTON)
                .setButtonText("Profile")
                .setButtonIconResource(R.drawable.ic_menu_manage)
                .setOnButtonClickListener("your_profile", null, onButtonClickListener)
                .setProgressBarColor(Color.WHITE)
                .setText(text)
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_PURPLE))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    private int totalDuration = 0;

    private final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            // Set the duration of the file.
            totalDuration = mp.getDuration();
            // set Progress bar values
            seekBar.setProgress(0);
            seekBar.setMax(100);
            updateProgressBar();
        }
    };

    private final android.media.MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d(TAG, "onCompletion");
            incrementPlayCount(); // Increment count for animal.
            // TODO: Move toast to incrementPlayCount callback.
            makeSuperToastOnIncrement("+1 " + animal);
            // Changing button image to play button
            playPauseButton.setImageResource(R.drawable.audio_play);
        }
    };

    private void playSoundFile() {
        String mp3File = new File(Constants.SOUND_FOLDER, fileName).toString();
        Log.d(TAG, "Playing " + animal + ": " + mp3File);
        if (player != null) {
            stopAndReleasePlayer();
        }
        player = new MediaPlayer();
        try {
            AssetFileDescriptor afd = aMan.openFd(mp3File);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.setOnCompletionListener(onCompletionListener);
            player.setOnPreparedListener(onPreparedListener);
            player.prepareAsync();
        } catch (Exception e) {
            Log.e("playSoundFile", e.toString());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        // Changing Button Image to pause image
        playPauseButton.setImageResource(R.drawable.audio_pause);
    }

//    http://stackoverflow.com/questions/16141167/android-audio-seekbar
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_sound, container, false);

//        Image = (ImageView) view.findViewById(R.id.pdfview);
//        Image.setImageResource(R.drawable.wereim);

        final MainActivity context = ((MainActivity) getActivity());
        androidId = context.getAndroidId();

        final Bundle bundle = getArguments();
        animal = bundle.getString("animal");
        fileName = bundle.getString("fileName");

        titleText = (TextView) view.findViewById(R.id.titleText);
        titleText.setText(animal);

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        songCurrentDurationLabel = (TextView) view.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) view.findViewById(R.id.songTotalDurationLabel);

        playPauseButton = (ImageButton) view.findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            if (player != null) {
                // Set the player button icon (play or pause depending on the current player status.
                if (player.isPlaying()) {
                    player.pause();
                    // Changing button image to play button
                    playPauseButton.setImageResource(R.drawable.audio_play);
                } else {
                    // Resume song
                    player.start();
                    // Changing button image to pause button
                    playPauseButton.setImageResource(R.drawable.audio_pause);
                }
            }

            }
        });

        backButton = (Button)  view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                context.launchFragment(SoundFragment.class.newInstance(), Constants.ENTER_LEFT);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
            try {
                handler.removeCallbacks(mUpdateTimeTask);
                stopAndReleasePlayer();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            }
        });

        aMan = getActivity().getAssets();
        handler = new Handler();
        playSoundFile();
        return view;
    }

    /**
     * Update timer on sound seekbar each 100ms.
     * */
    public void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            // long totalDuration = player.getDuration();
            try {
                long currentDuration = player.getCurrentPosition();
                // Displaying Total Duration time
                songTotalDurationLabel.setText(utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                songCurrentDurationLabel.setText(utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                int progress = utils.getProgressPercentage(currentDuration, totalDuration);
                //Log.d("Progress", ""+progress);
                seekBar.setProgress(progress);

                // Running this thread after 100 milliseconds
                handler.postDelayed(this, 100);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                stopAndReleasePlayer();
            }
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        handler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
        if (player!=null) {
            // int totalDuration = player.getDuration();
            int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

            // forward or backward to certain seconds
            player.seekTo(currentPosition);

            // update timer progress again
            updateProgressBar();
        }
    }

    private void stopAndReleasePlayer() {
        if (player != null) {
            try {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.release();
                player = null;
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        stopAndReleasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        stopAndReleasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        playSoundFile();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopAndReleasePlayer();
    }

}
