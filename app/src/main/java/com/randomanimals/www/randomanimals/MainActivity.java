package com.randomanimals.www.randomanimals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.randomanimals.www.randomanimals.fragments.AboutFragment;
import com.randomanimals.www.randomanimals.fragments.LeaderFragment;
import com.randomanimals.www.randomanimals.fragments.PlaySoundFragment;
import com.randomanimals.www.randomanimals.fragments.ProfileFragment;
import com.randomanimals.www.randomanimals.fragments.QuizFragment;
import com.randomanimals.www.randomanimals.fragments.ShareFragment;
import com.randomanimals.www.randomanimals.fragments.SoundListFragment;
import com.randomanimals.www.randomanimals.models.SoundFile;
import com.randomanimals.www.randomanimals.services.ValidatorUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private Class currentFragmentClass = SoundListFragment.class;

    public ArrayList<SoundFile> soundFiles = new ArrayList<>();

    private String androidId;
    public String getAndroidId() {
        return androidId;
    }

    public String username;

    public ArrayList<SoundFile> getSoundFiles() {
        return soundFiles;
    }

    private void initSoundFiles() {
        String[] files;
        try {
            files = getResources().getAssets().list(Constants.SOUND_FOLDER);
            Log.d(TAG, "files: " + Arrays.toString(files));
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            files =  new String[]{};
        }

        soundFiles.clear();
        int i = 0;
        for (String soundFile : files) {
            if (!soundFile.contains("mp3")) {
                continue;
            }

            try {
                String animal = soundFile.split("-")[0];
                animal = animal.replace("_", " ");
                soundFiles.add(new SoundFile(soundFile, animal, i++));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                Log.e(TAG, "Bad formatting of animal file, should be <animal>-<id>.mp3");
            }
        }
        Log.d(TAG, "Soundfiles: " + soundFiles.toString());
    }

    private String generateUserName() {
        return "guest" + Math.round(Math.random()*Integer.MAX_VALUE/100);
    }

    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (data != null)  {
            if (data.contains("random-animals")) {
                Log.d(TAG, "random animals onNewIntent");
            }
            // TODO: launch into sound fragment
            // launchSoundFragment(soundFiles.get(listPosition), listPosition);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5780102660170607~3225694773");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        // Setup.
        androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        username = getStringPref(Constants.USERNAME_KEY);
        if (!ValidatorUtil.validateUsername(username)) {
            saveStringPref(Constants.USERNAME_KEY, generateUserName());
        }

        initSoundFiles();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNewIntent(getIntent());

        try {
            launchFragment(SoundListFragment.class.newInstance(), Constants.ENTER_LEFT);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        // Initialize ad banner at bottom of page.
        AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder()
                .setGender(AdRequest.GENDER_MALE)
                .addKeyword("animal")
                .setIsDesignedForFamilies(true)
                .build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected " + item.toString());
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean launchFragment(Fragment fragment, int enter) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment.getClass() != currentFragmentClass) {
            if (enter == Constants.ENTER_LEFT) {
                transaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
            } else if (enter == Constants.ENTER_RIGHT) {
                transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
            }
        }

        currentFragmentClass = fragment.getClass();

        transaction.replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean launchSoundFragment(SoundFile soundFile, final int listPosition, final int bonus) {
        final Fragment fragment;
        try {
            fragment = PlaySoundFragment.class.newInstance();
            setTitle(soundFile.animal);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return false;
        }

        Bundle args = new Bundle();
        args.putString("animal", soundFile.animal);
        args.putString("fileName", soundFile.fileName);
        args.putInt("listPosition", listPosition);
        args.putInt("bonus", bonus);
        fragment.setArguments(args);
        return launchFragment(fragment, Constants.ENTER_RIGHT);
    }

    public boolean launchLeaderFragment(final int listPosition) {
        final Fragment fragment;
        try {
            fragment = LeaderFragment.class.newInstance();
            setTitle("Animal Sounds Leaderboards");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return false;
        }

        Bundle args = new Bundle();
        args.putInt("listPosition", listPosition);
        fragment.setArguments(args);
        return launchFragment(fragment, Constants.ENTER_RIGHT);
    }

//    http://guides.codepath.com/android/fragment-navigation-drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment;
        Class fragmentClass;

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d(TAG, "onNavigationItemSelected " + item.toString());

        // Select fragment based on user choice.
        final String title;
        switch (id) {
            case R.id.nav_sounds:
                fragmentClass = SoundListFragment.class;
                title = "Animal Sounds";
                break;
            case R.id.nav_leader:
                fragmentClass = LeaderFragment.class;
                title = "Animal Sounds Leaderboards";
                break;
            case R.id.nav_quiz:
                fragmentClass = QuizFragment.class;
                title = "Animal Sound Quiz";
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                title = "Profile";
                break;
            case R.id.nav_share:
                fragmentClass = ShareFragment.class;
                title = "Share RandomAnimals";
                break;
            case R.id.nav_about:
                fragmentClass = AboutFragment.class;
                title = "Special Thanks";
                break;
            default:
                fragmentClass = SoundListFragment.class;
                title = "Animal Sounds";
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            setTitle(title);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return false;
        }
        return launchFragment(fragment, Constants.ENTER_LEFT);
    }

//    if (!ValidatorUtil.validateUsername(value)) {
//        value = generateUserName();
//        SharedPreferences.Editor prefsEditor = prefs.edit();
//        prefsEditor.putString(key, value);
//        prefsEditor.apply();
//    }

    public String getStringPref(String key) {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public void saveStringPref(String key, String value) {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
        Log.d(TAG, "Set " + key + ": " + value);
    }


}
