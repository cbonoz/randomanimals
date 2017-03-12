package com.randomanimals.www.randomanimals.fragments;


import android.content.Intent;
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
import com.randomanimals.www.randomanimals.services.WebService;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

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

    String animal;
    String fileName;

    public PlaySoundFragment() {
        // Required empty public constructor
    }

    private String androidId;

    private void incrementPlayCount() {
        Intent incIntent = new Intent(getActivity(), WebService.class);
        try {
            JSONObject incJson = new JSONObject();
            incJson.put("userid", androidId);
            incJson.put("animal", animal);

            incIntent.putExtra("url", Constants.PROFILE_URL);
            incIntent.putExtra("body", incJson.toString());
            ((MainActivity) getActivity()).startService(incIntent);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void playSoundFile() {
        final String clickText = "Sound: " + fileName;
        Toast.makeText(getActivity(), clickText, Toast.LENGTH_SHORT).show();
        String mp3File = new File(Constants.SOUND_FOLDER, fileName).toString();
        Log.d(TAG, "Playing " + animal + ": " + mp3File);

        FileInputStream fis = null;
        try {
//            fis = new FileInputStream(mp3File);
//            player.setDataSource(fis.getFD());
            AssetFileDescriptor afd = aMan.openFd(mp3File);
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
        } catch (Exception e) {
            Log.e("playSoundFile", e.toString());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ignore) {
                }
            }
        }

        buttonPlayPause
                .setBackgroundResource(android.R.drawable.ic_media_play);
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
        seekBar.setOnSeekBarChangeListener(seekBarOnSeekChangeListener);

        buttonPlayPause = (Button) view.findViewById(R.id.playPauseButton);
        buttonPlayPause.setOnClickListener(buttonPlayPauseOnClickListener);

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
            }
        });


        aMan = getActivity().getAssets();
        initMediaPlayer();
        playSoundFile();
        incrementPlayCount();
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

//    private void incrementPlayCount() {
//        MainActivity context = ((MainActivity) getActivity());
//        context.userInfo.incrementPlays(fileName);
//        context.saveUserInfo();
//    }

    Button.OnClickListener buttonPlayPauseOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (stateMediaPlayer) {
                case stateMP_NotStarter:
                    player.start();
                    buttonPlayPause
                            .setBackgroundResource(android.R.drawable.ic_media_pause);
                    stateMediaPlayer = stateMP_Playing;
                    incrementPlayCount();
                    break;
                case stateMP_Playing:
                    player.pause();
                    buttonPlayPause
                            .setBackgroundResource(android.R.drawable.ic_media_play);
                    stateMediaPlayer = stateMP_Pausing;
                    break;
                case stateMP_Pausing:
                    player.start();
                    buttonPlayPause
                            .setBackgroundResource(android.R.drawable.ic_media_pause);
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
            // TODO Auto-generated method stub

            if (fromUser) {
                player.seekTo(progress);
                seekBar.setProgress(progress);
            }

        }
    };

}
