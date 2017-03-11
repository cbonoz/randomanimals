package com.randomanimals.www.randomanimals;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.fragments.PlaySoundFragment;
import com.randomanimals.www.randomanimals.fragments.QuizFragment;
import com.randomanimals.www.randomanimals.fragments.SoundFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private Class currentFragmentClass = SoundFragment.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        try {
            launchFragment(SoundFragment.class.newInstance(), Constants.ENTER_LEFT);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(this, e.toString() + " - try again later", Toast.LENGTH_SHORT).show();
        }
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
//        // Highlight the selected item has been done by NavigationView
//        menuItem.setChecked(true);
//        setTitle(menuItem.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean launchSoundFragment(String soundFile) {
        final Class fragmentClass = PlaySoundFragment.class;
        String soundTitle = soundFile;
        Fragment fragment;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            setTitle(soundTitle);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return false;
        }
        Bundle args = new Bundle();
        args.putString("soundFile", soundFile);
        args.putString("soundTitle", soundTitle);
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
                fragmentClass = SoundFragment.class;
                title = "Animal Sounds";
                break;
            case R.id.nav_quiz:
                fragmentClass = QuizFragment.class;
                title = "Animal Sound Quiz";
                break;
            default:
                fragmentClass = SoundFragment.class;
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
}
