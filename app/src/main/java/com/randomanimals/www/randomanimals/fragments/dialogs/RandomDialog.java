package com.randomanimals.www.randomanimals.fragments.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.models.SoundFile;
import com.randomanimals.www.randomanimals.services.NumberUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

import static com.randomanimals.www.randomanimals.R.id.luckyWheel;
import static com.randomanimals.www.randomanimals.services.NumberUtil.getNextBonus;
import static com.randomanimals.www.randomanimals.services.NumberUtil.getRandomIndex;
import static com.randomanimals.www.randomanimals.services.NumberUtil.getRandomNumberOfRotations;

public class RandomDialog extends DialogFragment {
    private static final String TAG = "RandomDialog";

    private static final int MAX_ITEMS = 24;

    private static final List<Integer> ANIMAL_IMAGES = Arrays.asList(
            R.drawable.test1,
            R.drawable.test2,
            R.drawable.bulldog,
            R.drawable.butterfly,
            R.drawable.test4,
            R.drawable.test5,
            R.drawable.test6,
            R.drawable.test7,
            R.drawable.test8,
            R.drawable.test9,
            R.drawable.test10
    );

    private List<LuckyItem> data;
    private LuckyWheelView luckyWheelView;

    // UI elements.
    private Button playButton;

    public static RandomDialog newInstance(int num) {
        RandomDialog f = new RandomDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // experiment with style and theme.
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar);
    }

    private List<SoundFile> soundFiles;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.random_dialog, container, false);

        luckyWheelView = (LuckyWheelView) view.findViewById(luckyWheel);

        ImageButton dialogHeader = (ImageButton) view.findViewById(R.id.dialogHeader);
        dialogHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /////////////////////
        final MainActivity activity = (MainActivity) getActivity();
        soundFiles = activity.getSoundFiles();

        data = new ArrayList<>();
        loadWheelData();

        luckyWheelView.setData(data);
        luckyWheelView.bringToFront();
        luckyWheelView.setRound(getRandomNumberOfRotations());

        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/

        playButton = (Button) view.findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = getRandomIndex(data.size());
                luckyWheelView.startLuckyWheelWithTargetIndex(index);
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                final int bonus = data.get(index).bonus;
                // Dismiss the dialog and launch the sound Fragment with the bonus
                dismiss();
                launchSoundFragmentForSound(index, bonus);
            }
        });

        return view;
    }

    private void loadWheelData() {
        final int numItems = Math.min(MAX_ITEMS, soundFiles.size());
        final int remaining = soundFiles.size() - numItems;
        final int startIndex = NumberUtil.getRandomIndex(remaining);

        final int randomIcon = ANIMAL_IMAGES.get(getRandomIndex(ANIMAL_IMAGES.size()));

        for (int i = startIndex; i < startIndex + numItems; i++) {
            LuckyItem luckyItem = new LuckyItem();
            if (i % 2 == 0) {
                luckyItem.color = 0xffFFE0B2;
            } else if (i % 3 == 0) {
                luckyItem.color = 0xffFFCC80;
            } else {
                luckyItem.color = 0xffFFF3E0;
            }
            luckyItem.soundListPosition = i;
            final int bonus = getNextBonus();
            luckyItem.text = bonus + "";
            luckyItem.icon = randomIcon;
            luckyItem.bonus = bonus;
            data.add(luckyItem);
        }

    }

    private void launchSoundFragmentForSound(final int position, final int bonus) {
        SoundFile soundFile = soundFiles.get(position);
        final String message = String.format(Locale.getDefault(), "%s: %d!", soundFile.animal, bonus);
        final MainActivity activity = (MainActivity) getActivity();
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        activity.launchSoundFragment(soundFile, position, bonus);
    }
}
