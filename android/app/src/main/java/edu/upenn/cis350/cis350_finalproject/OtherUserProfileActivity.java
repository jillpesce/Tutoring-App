package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database_schema.Date;
import database_schema.Timeslot;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class OtherUserProfileActivity extends AppCompatActivity {
    User currentUser;
    User profileUser;
    List<Timeslot> timeslots;
    int timeslotIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        String profileUserEmail = getIntent().getStringExtra("PROFILE USER");
        String currentUserEmail = getIntent().getStringExtra("CURR USER EMAIL");
        RemoteDataSource rd = new RemoteDataSource();
        profileUser = rd.findUser(profileUserEmail);
        currentUser = rd.findUser(currentUserEmail);
        timeslotIndex = 0;

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

        timeslots = new ArrayList<Timeslot>();
        getAndFilterTimeSlots();
        setTimeslotData(timeslotIndex);
    }

    private void getAndFilterTimeSlots() {
        RemoteDataSource ds = new RemoteDataSource();
        List<Timeslot> allTimeslots = ds.getAllTimeslots();
        for (Timeslot t : allTimeslots) {
            if (t.getTutor().equals(profileUser.getEmail())) {
                timeslots.add(t);
                Log.d("TIMESLOT ADD", "added time slot " + timeslots.toString());
            }
        }
        Collections.sort(timeslots);
        Log.d("TIMESLOT SIZE", "" + timeslots.size());
    }

    private void setTimeslotData(int index) {
        TextView date = (TextView) findViewById(R.id.timeslot_date);
        TextView courses = (TextView) findViewById(R.id.timeslot_courses);
        if (index < timeslots.size()) {
            Date d = new Date(timeslots.get(index).getDate());
            date.setText(d.getFullDescription());
            if (timeslots.get(index).getCourses().length > 0) {
                courses.setText("Courses: " + timeslots.get(index).getCoursesString());
            }
        } else if (timeslots.size() == 0) {
            date.setText("No availability");
            disableNextButton();
            disablePrevButton();
            hideSelectButton();
        }
        //check if there are still available timeslots to view
        if (timeslotIndex + 1 >= timeslots.size()) {
            disableNextButton();
        } else {
            enableNextButton();
        }

        if (timeslotIndex - 1 < 0) {
            disablePrevButton();
        } else {
            enablePrevButton();
        }
    }

    public void disableNextButton() {
        Button next = findViewById(R.id.button_next);
        next.setEnabled(false);
    }

    public void enableNextButton() {
        Button next = findViewById(R.id.button_next);
        next.setEnabled(true);
    }

    public void disablePrevButton() {
        Button prev = findViewById(R.id.button_prev);
        prev.setEnabled(false);
    }

    public void enablePrevButton() {
        Button prev = findViewById(R.id.button_prev);
        prev.setEnabled(true);
    }

    public void onBackButtonClicked(View v) {
        Intent intent = new Intent();
        finish();
    }

    public void onNextButtonclicked(View v) {
        timeslotIndex++;
        setTimeslotData(timeslotIndex);
    }

    public void hideSelectButton() {
        Button select = findViewById(R.id.select_appt);
        select.setVisibility(View.INVISIBLE);
    }

    public void onSelectButtonClicked(View v) {
        Intent intent = new Intent(this, RequestAppointmentActivity.class);
        intent.putExtra("TUTOR_EMAIL", profileUser.getEmail());
        intent.putExtra("TUTOR_NAME", profileUser.getName());
        intent.putExtra("TUTEE_EMAIL", currentUser.getEmail());
        intent.putExtra("TUTEE_NAME", currentUser.getName());
        intent.putExtra("DATE", timeslots.get(timeslotIndex).getDate());
        startActivity(intent);
    }

    public void onPrevButtonClicked(View v) {
        timeslotIndex--;
        setTimeslotData(timeslotIndex);
    }



}
