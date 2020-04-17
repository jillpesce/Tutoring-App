package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;

import database_schema.User;
import database_schema.Appointment;

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
        TextView rating = findViewById(R.id.profile_rating);


        name.setText(profileUser.getName());
        bio.setText(profileUser.getBio());
        school.setText("School: " + profileUser.getSchool());
        major.setText("Major: " + profileUser.getMajor());
        gradYear.setText("Graduation Year: " + profileUser.getGradYear());
        email.setText("Email: " + profileUser.getEmail());
        ArrayList<Integer> ratings = rd.getUserRatings(profileUser);

        if (ratings.isEmpty()) {
            rating.setText("Rating: User has not been rated!");
        }
        double sumRating = 0;
        Log.d("Test", "hello just testing");
        Log.d("ratings size", "" + ratings.size());
        for (int i = 0; i < ratings.size(); i++) {
            Log.d("hello", "" + ratings.get(i));
            sumRating += Integer.parseInt("" + ratings.get(i));
        }
        double avgRating = sumRating / ratings.size();
        Log.d("Selected", "" + avgRating);

        rating.setText("Rating: " + avgRating);
    }

    public void onBackButtonClicked(View v) {
        Intent intent = new Intent();
        finish();
    }

    public void onRateButtonClicked(View v) {
        String currentUserEmail = getIntent().getStringExtra("CURR USER EMAIL");
        RemoteDataSource rd = new RemoteDataSource();
        currentUser = rd.findUser(currentUserEmail);
        ArrayList<Appointment> appointments;
        Button rateButton = (Button) findViewById(R.id.rateButton);

        if (currentUser.getIsTutor()) { //TUTOR MODE
            appointments = (ArrayList) rd.getTutorAppointments(currentUser.getEmail());
            boolean hadAppointment = false;
            for (int i = 0; i < appointments.size(); i++) {
                if (profileUser.getEmail().equals(appointments.get(i).getTuteeEmail())) {
                    hadAppointment = true;
                }
            }
            if (hadAppointment) {
                RatingBar stars = (RatingBar) findViewById(R.id.ratingBar);
                int currStars = (int) stars.getRating();
                Log.d("Stars: ", "" + currStars);
                // update db
                rd.saveRating(profileUser, currStars);
                finish();
                startActivity(getIntent());
//                rateButton.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        RatingBar stars = (RatingBar) findViewById(R.id.ratingBar);
//                        int currStars = (int) stars.getRating();
//                        Log.d("Stars: ", "" + currStars);
//                        // update db
//                        RemoteDataSource rd = new RemoteDataSource();
//                        rd.saveRating(profileUser, currStars);
//                    }
//                });
            } else {
                RatingBar stars = (RatingBar) findViewById(R.id.ratingBar);
                int currStars = (int) stars.getRating();
                Log.d("Stars: ", "" + currStars);
                Toast.makeText(
                        this,
                        "Sorry! It appears that you've never tutored this " +
                                "user.  You cannot rate them.",
                        Toast.LENGTH_LONG
                ).show();
                new AsyncTask<String, String, String>() {
                    protected String doInBackground(String... inputs) {
                        try {
                            Thread.sleep(6000);
                        } catch (Exception e) { }
                        return null;
                    }
                    protected void onPostExecute(String input) {
                        // nothing
                    }
                }.execute();
            }
        } else { // TUTEE MODE
            appointments = (ArrayList) rd.getTuteeAppointments(currentUser.getEmail());
            boolean hadAppointment = false;
            for (int i = 0; i < appointments.size(); i++) {
                if (profileUser.getEmail().equals(appointments.get(i).getTutorEmail())) {
                    hadAppointment = true;
                }
            }

            if (hadAppointment) {
                RatingBar stars = (RatingBar) findViewById(R.id.ratingBar);
                int currStars = (int) stars.getRating();
                Log.d("Stars: ", "" + currStars);
                // update db
                rd.saveRating(profileUser, currStars);
                finish();
                startActivity(getIntent());
//                rateButton.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        RatingBar stars = (RatingBar) findViewById(R.id.ratingBar);
//                        int currStars = (int) stars.getRating();
//                        Log.d("Stars: ", "" + currStars);
//                        // update db
//                        RemoteDataSource rd = new RemoteDataSource();
//                        rd.saveRating(profileUser, currStars);
//                    }
//                });

            } else {
                RatingBar stars = (RatingBar) findViewById(R.id.ratingBar);
                int currStars = (int) stars.getRating();
                Log.d("Stars: ", "" + currStars);
                Toast.makeText(
                        this,
                        "Sorry! It appears that this user has never tutored you." +
                                "  You cannot rate them.",
                        Toast.LENGTH_LONG
                ).show();
                new AsyncTask<String, String, String>() {
                    protected String doInBackground(String... inputs) {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) { }
                        return null;
                    }
                    protected void onPostExecute(String input) {
                        // nothing
                    }
                }.execute();
            }
        }
    }

}
