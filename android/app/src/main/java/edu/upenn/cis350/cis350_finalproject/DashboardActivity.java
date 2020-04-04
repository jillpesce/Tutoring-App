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
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import database_schema.User;
import datamanagement.RemoteDataSource;

public class DashboardActivity extends AppCompatActivity {
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String email = getIntent().getStringExtra("EMAIL");

        user = createFakeUserForNow();
//        RemoteDataSource ds = new RemoteDataSource();
//        this.user = ds.findUser(email);
//        Log.d("USER_DASHBOARD", user.toString());

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
            FragmentManager fm = null;
            switch(item.getItemId()) {
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    ((ProfileFragment) selectedFragment).setUser(user);
                    fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
                    break;
                case R.id.nav_schedule:
                    selectedFragment = new TimeslotsFragment();
                    ((TimeslotsFragment) selectedFragment).setUser(user);
                    fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
                    break;
            }
            return true;
        }
    };

    public void onSignOutButtonClick(View v) {
        Intent signOutIntent = new Intent();
        setResult(RESULT_OK, signOutIntent);
        finish();
    }
}
