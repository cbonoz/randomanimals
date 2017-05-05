package com.randomanimals.www.randomanimals.fragments.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.models.SoundFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

import static com.randomanimals.www.randomanimals.R.id.luckyWheel;

public class RandomDialog extends DialogFragment {
    private static final String TAG = "RandomDialog";

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

    List<LuckyItem> data = new ArrayList<>();

    private LuckyWheelView luckyWheelView;

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
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    private List<SoundFile> soundFiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.random_dialog, container, false);

        luckyWheelView = (LuckyWheelView) view.findViewById(luckyWheel);

        /////////////////////
        final MainActivity activity = (MainActivity) getActivity();
        soundFiles = activity.getSoundFiles();

        final int numItems = Math.min(24, soundFiles.size());
        final int randomIcon = ANIMAL_IMAGES.get(getRandomIndex(ANIMAL_IMAGES));

        for (int i = 0; i < numItems; i++) {
            LuckyItem luckyItem = new LuckyItem();
            SoundFile soundFile = soundFiles.get(i);
            if (i % 2 == 0) {
                luckyItem.color = 0xffFFE0B2;
            } else if (i % 3 == 0) {
                luckyItem.color = 0xffFFCC80;
            } else {
                luckyItem.color = 0xffFFF3E0;
            }
            luckyItem.text = Integer.toString(soundFile.listPosition+1);
            luckyItem.icon = randomIcon;
            data.add(luckyItem);
        }

        luckyWheelView.setData(data);
        luckyWheelView.bringToFront();
        luckyWheelView.setRound(getRandomNumberOfRotations());

        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/


        view.findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = getRandomIndex(data);
                luckyWheelView.startLuckyWheelWithTargetIndex(index);
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                dismiss();
                launchSoundFragmentForSound(index);
            }
        });

        return view;
    }

    private <T> int getRandomIndex(List<T> arr) {
        Random rand = new Random();
        return rand.nextInt(arr.size() - 1);
    }

    private int getRandomNumberOfRotations() {
        Random rand = new Random();
        return rand.nextInt(10) + 5;
    }

    private void launchSoundFragmentForSound(int position) {
        SoundFile soundFile = soundFiles.get(position);
        final int bonus = position + 1;
        final String message = String.format("%s: %d!", soundFile.animal, bonus);
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        ((MainActivity) getActivity()).launchSoundFragment(soundFile, position, bonus);
    }


//        ButterKnife.bind(this, view);
//        soundFiles = ((MainActivity) getActivity()).getSoundFiles();
//        final int numItems = Math.min(soundFiles.size(), 10);
//        createLuckyWheelItems(numItems);
//        return view;
//    }
//

    public void createLuckyWheelItems(int n) {


        List<LuckyItem> data = new ArrayList<>();

    }

    private void createExampleWheelData() {
        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "100";
        luckyItem1.icon = R.drawable.test1;
        luckyItem1.color = 0xffFFF3E0;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "200";
        luckyItem2.icon = R.drawable.test2;
        luckyItem2.color = 0xffFFE0B2;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "300";
        luckyItem3.icon = R.drawable.test3;
        luckyItem3.color = 0xffFFCC80;
        data.add(luckyItem3);

        //////////////////
        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "400";
        luckyItem4.icon = R.drawable.test4;
        luckyItem4.color = 0xffFFF3E0;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "500";
        luckyItem5.icon = R.drawable.test5;
        luckyItem5.color = 0xffFFE0B2;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "600";
        luckyItem6.icon = R.drawable.test6;
        luckyItem6.color = 0xffFFCC80;
        data.add(luckyItem6);
        //////////////////

        //////////////////////
        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "700";
        luckyItem7.icon = R.drawable.test7;
        luckyItem7.color = 0xffFFF3E0;
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "800";
        luckyItem8.icon = R.drawable.test8;
        luckyItem8.color = 0xffFFE0B2;
        data.add(luckyItem8);


        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.text = "900";
        luckyItem9.icon = R.drawable.test9;
        luckyItem9.color = 0xffFFCC80;
        data.add(luckyItem9);
        ////////////////////////

        LuckyItem luckyItem10 = new LuckyItem();
        luckyItem10.text = "1000";
        luckyItem10.icon = R.drawable.test10;
        luckyItem10.color = 0xffFFE0B2;
        data.add(luckyItem10);

        LuckyItem luckyItem11 = new LuckyItem();
        luckyItem11.text = "2000";
        luckyItem11.icon = R.drawable.test10;
        luckyItem11.color = 0xffFFE0B2;
        data.add(luckyItem11);

        LuckyItem luckyItem12 = new LuckyItem();
        luckyItem12.text = "3000";
        luckyItem12.icon = R.drawable.test10;
        luckyItem12.color = 0xffFFE0B2;
        data.add(luckyItem12);
    }
}
