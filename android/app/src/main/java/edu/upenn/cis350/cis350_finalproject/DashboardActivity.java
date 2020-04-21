package edu.upenn.cis350.cis350_finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import database_schema.Timeslot;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class DashboardActivity extends AppCompatActivity {
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String email = getIntent().getStringExtra("EMAIL");
        //get the user from the database
//        // TODO actually get the saved user's email
        RemoteDataSource ds = new RemoteDataSource();
        this.user = ds.findUser(email);
//        this.user = createFakeUserForNow();

        AppointmentsTuteeFragment selectedFragment = new AppointmentsTuteeFragment();
        FragmentManager fm = getSupportFragmentManager();
        selectedFragment.setUser(user);
        fm.beginTransaction().replace(R.id.frame_layout, selectedFragment, "appt_tutee_frag").commit();

        BottomNavigationView bottomNav = findViewById(R.id.btm_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private User createFakeUserForNow() {
        User user = new User("pchloe@seas.upenn.edu", "Chloe Prezelski", "SEAS",
                "CIS", "2021", "This is my bio.");
        return user;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == Activity.RESULT_OK)) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("appt_tutee_frag");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
            Log.d("REFRESH", "Should have been refreshed");
        } else if ((requestCode == 2) && (resultCode == Activity.RESULT_OK)) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("appt_tutor_frag");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
            Log.d("REFRESH", "Should have been refreshed");
        } else if ((requestCode == 5) && (resultCode == Activity.RESULT_OK)) {
            // after filter
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("timeslot_tutee_frag");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
            Log.d("REFRESH", "Should have been refreshed");
        } else if ((requestCode == 3) && (resultCode == Activity.RESULT_OK)) {
            // after add new timeslot
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("timeslot_tutor_frag");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
            Log.d("REFRESH", "Should have been refreshed");
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment selectedFragment = null;
            FragmentManager fm = getSupportFragmentManager();
            switch(item.getItemId()) {
                case R.id.nav_home:
                    if (user.getIsTutor()) {
                        selectedFragment = new AppointmentsTutorFragment();
                        ((AppointmentsTutorFragment) selectedFragment).setUser(user);
                        fm.beginTransaction().replace(R.id.frame_layout, selectedFragment,
                                "appt_tutor_frag").commit();
                    } else {
                        selectedFragment = new AppointmentsTuteeFragment();
                        ((AppointmentsTuteeFragment) selectedFragment).setUser(user);
                        fm.beginTransaction().replace(R.id.frame_layout, selectedFragment,
                                "appt_tutee_frag").commit();
                    }
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    ((ProfileFragment) selectedFragment).setUser(user);
                    fm.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
                    break;
                case R.id.nav_schedule:
                    if (user.getIsTutor()) {
                        selectedFragment = new TutorTimeslotsFragment();
                        ((TutorTimeslotsFragment) selectedFragment).setUser(user);
                        fm.beginTransaction().replace(R.id.frame_layout, selectedFragment, "timeslot_tutor_frag").commit();
                    } else {
                        selectedFragment = new TimeslotsFragment();
                        ((TimeslotsFragment) selectedFragment).setUser(user);
                        fm.beginTransaction().replace(R.id.frame_layout, selectedFragment, "timeslot_tutee_frag").commit();
                    }
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    ((SearchFragment) selectedFragment).setUser(user);
                    fm.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
                    break;
            }
//            fm.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
            return true;
        }
    };

    public void onSignOutButtonClick(View v) {
        Intent signOutIntent = new Intent();
        setResult(RESULT_OK, signOutIntent);
        finish();
    }

    public void onToggleButtonClick(View v) {
        user.setIsTutor(!user.getIsTutor());
        Log.d("toggle", "" + user.getIsTutor());
        String text = "Toggle to " + (user.getIsTutor() ? "Tutee" : "Tutor");
        ((TextView) v).setText(text);
    }

    public User getUser() {
        return this.user;
    }
}
