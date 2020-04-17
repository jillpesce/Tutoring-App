package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import database_schema.User;
import datamanagement.RemoteDataSource;

public class OtherUserProfileActivity extends AppCompatActivity {
    User currentUser;
    User profileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        String profileUserEmail = getIntent().getStringExtra("PROFILE USER");
        RemoteDataSource rd = new RemoteDataSource();
        profileUser = rd.findUser(profileUserEmail);

        TextView name = findViewById(R.id.profile_name);
        TextView bio = findViewById(R.id.profile_bio);
        TextView school = findViewById(R.id.profile_school);
        TextView major = findViewById(R.id.profile_major);
        TextView gradYear = findViewById(R.id.profile_gradYear);
        TextView email = findViewById(R.id.profile_email);

        name.setText(profileUser.getName());
        bio.setText(profileUser.getBio());
        school.setText("School: " + profileUser.getSchool());
        major.setText("Major: " + profileUser.getMajor());
        gradYear.setText("Graduation Year: " + profileUser.getGradYear());
        email.setText("Email: " + profileUser.getEmail());
    }

    public void onBackButtonClicked(View v) {
        Intent intent = new Intent();
        finish();
    }



}
