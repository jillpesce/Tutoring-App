package edu.upenn.cis350.cis350_finalproject;

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
        // TODO actually get the saved user's email
        RemoteDataSource ds = new RemoteDataSource();
        this.user = ds.findUser(email);
        //this.user = createFakeUserForNow();

        AppointmentsTuteeFragment selectedFragment = new AppointmentsTuteeFragment();
        FragmentManager fm = getSupportFragmentManager();
        selectedFragment.setUser(user);
        fm.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();

        BottomNavigationView bottomNav = findViewById(R.id.btm_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private User createFakeUserForNow() {
        User user = new User("pchloe@seas.upenn.edu", "Chloe Prezelski", "SEAS",
                "CIS", "2021", "This is my bio.");
        return user;
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
                    } else {
                        selectedFragment = new AppointmentsTuteeFragment();
                        ((AppointmentsTuteeFragment) selectedFragment).setUser(user);
                    }
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    ((ProfileFragment) selectedFragment).setUser(user);
                    break;
                case R.id.nav_schedule:
                    if (user.getIsTutor()) {
                        selectedFragment = new TutorTimeslotsFragment();
                        ((TutorTimeslotsFragment) selectedFragment).setUser(user);
                    } else {
                        selectedFragment = new TimeslotsFragment();
                        ((TimeslotsFragment) selectedFragment).setUser(user);
                    }
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
            }
            fm.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
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
}
