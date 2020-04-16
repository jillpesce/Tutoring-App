package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import database_schema.User;

public class OtherUserProfileActivity extends AppCompatActivity {
    User currentUser;
    User profileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String currentUserEmail = getIntent().getStringExtra("CURR EMAIL");
        String profileUserEmail = getIntent().getStringExtra("PROFILE USER");
    }

    public void onBackButtonClicked(View v) {
        Intent intent = new Intent();
        finish();
    }

}
