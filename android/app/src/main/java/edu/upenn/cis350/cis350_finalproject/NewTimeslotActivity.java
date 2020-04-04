package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.DatePicker;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import database_schema.Timeslot;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class NewTimeslotActivity extends AppCompatActivity {
    String tutorEmail;
    String tutorName;
    int month;
    int day;
    int year;
    int hour;
    DatePicker picker;
    String[] courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INTENT", "jkdlsjf we got here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timeslot);
        tutorEmail = getIntent().getStringExtra("EMAIL");
        tutorName = getIntent().getStringExtra("NAME");
        courses = getIntent().getStringArrayExtra("COURSES");

        Spinner spinner = findViewById(R.id.timeslot_spinner);
        spinner.setOnItemSelectedListener(new SpinnerListener());

        picker = (DatePicker) findViewById(R.id.datePicker);
    }

    public class SpinnerListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String item = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    /**
     *
     * @param v
     * Called when submit button is pressed. Initiates request to create a profile.
     */
    public void onSubmitButtonClicked(View v) {
        Spinner t = findViewById(R.id.timeslot_spinner);
        month = picker.getMonth();
        day = picker.getDayOfMonth();
        year = picker.getYear();

        handleNewTimeslot(tutorEmail, month, day, year, t.getSelectedItem().toString().substring(0,2));
    }

    private void handleNewTimeslot(String email, int m, int d, int year, String hour) {
        if (emptyOrNull(email)) {
            Toast.makeText(getApplicationContext(), "Please fill in all required fields",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            //save timeslot to database

            String month = "" + m;
            String day = "" + d;

            if (m < 10) {
                month = "0" + month;
            }

            if (d < 10) {
                day = "0" + day;
            }

            String date = "" + year + month + day + hour;
            Log.d("DATE", date);
            RemoteDataSource ds = new RemoteDataSource();
            Timeslot t = new Timeslot(email, date, courses, tutorName);
            ds.saveNewTimeslot(t);
            finish();
        }
    }

    /**
     *
     * @param s
     * @return true if the string is null or empty and false otherwise
     */
    private boolean emptyOrNull(String s) {
        if (s == null) {
            return true;
        }
        return s.isEmpty();
    }
}
