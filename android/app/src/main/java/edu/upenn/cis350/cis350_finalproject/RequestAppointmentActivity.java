package edu.upenn.cis350.cis350_finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import database_schema.Appointment;
import database_schema.Date;
import database_schema.Timeslot;
import datamanagement.RemoteDataSource;

import androidx.appcompat.app.AppCompatActivity;

public class RequestAppointmentActivity extends AppCompatActivity {
    String tutorEmail;
    String tutorName;
    String tuteeEmail;
    String tuteeName;
    String dateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_appointment);
        tutorEmail = getIntent().getStringExtra("TUTOR_EMAIL");
        tutorName = getIntent().getStringExtra("TUTOR_NAME");
        tuteeEmail = getIntent().getStringExtra("TUTEE_EMAIL");
        tuteeName = getIntent().getStringExtra("TUTEE_NAME");
        dateAndTime = getIntent().getStringExtra("DATE");

        Date d = new Date(dateAndTime);

        TextView torName = (TextView) findViewById(R.id.tutor_name);
        torName.setText("Tutor: " +tutorName);

        TextView date = (TextView) findViewById(R.id.date);
        date.setText("Date: " + d.getDateDescription());

        TextView time = (TextView) findViewById(R.id.time);
        time.setText("Time: " + d.getTimeString());
    }


    public void makeRequest(View v) {
        handleNewTimeslot();
    }

    public void back(View v) {
        finish();
    }

    private void handleNewTimeslot() {
        RemoteDataSource ds = new RemoteDataSource();
        Appointment a = new Appointment(tutorName, tuteeName, dateAndTime, tutorEmail, tuteeEmail);
        ds.deleteTimeslot(tutorEmail,dateAndTime);
        ds.saveNewAppt(a);
        finish();
    }
}
