package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import database_schema.User;
import datamanagement.RemoteDataSource;

public class ReviewUserActivity extends AppCompatActivity {
    User currentUser;
    User profileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_user);

        String currentUserEmail = getIntent().getStringExtra("CURR USER EMAIL");
        String profileUserEmail = getIntent().getStringExtra("PROFILE USER EMAIL");

        RemoteDataSource rd = new RemoteDataSource();
        currentUser = rd.findUser(currentUserEmail);
        profileUser= rd.findUser(profileUserEmail);
        Log.d("CURR USER", currentUser.toString());
        Log.d("PROFILE USER", profileUser.toString());
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    public void onSubmitButtonClicked(View v) {
        //save the review to the database
        if (reviewCompleted()) {
            RemoteDataSource rd = new RemoteDataSource();
            EditText editText = findViewById(R.id.reviewEditText);
            String review = editText.getText().toString();
            String reviewBy = "     -" + currentUser.getName();
            review += reviewBy;
            RatingBar stars = findViewById(R.id.ratingBar);
            int currStars = (int) stars.getRating();
            rd.saveRating(profileUser, currStars, review);
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            finish();
        }
    }

    public boolean reviewCompleted() {
        EditText review = findViewById(R.id.reviewEditText);
        RatingBar stars = findViewById(R.id.ratingBar);
        if (review.getText().toString() == null || review.getText().toString().isEmpty()) {
            Toast.makeText(
                    this,
                    "Please give a review to complete the review!",
                    Toast.LENGTH_LONG
            ).show();
            return false;
        } else if ((int) stars.getRating() == 0) {
            Toast.makeText(
                    this,
                    "Please give a start rating to complete the review!",
                    Toast.LENGTH_LONG
            ).show();
            return false;
        }
        return true;
    }
}
