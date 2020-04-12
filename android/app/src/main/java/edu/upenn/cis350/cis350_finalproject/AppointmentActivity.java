package edu.upenn.cis350.cis350_finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import database_schema.Appointment;
import database_schema.Date;
import datamanagement.RemoteDataSource;

public class AppointmentActivity extends AppCompatActivity {
    String tutorEmail;
    String tutorName;
    String tuteeEmail;
    String tuteeName;
    String dateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        tutorEmail = getIntent().getStringExtra("TUTOR_EMAIL");
        tutorName = getIntent().getStringExtra("TUTOR_NAME");
        tuteeEmail = getIntent().getStringExtra("TUTEE_EMAIL");
        tuteeName = getIntent().getStringExtra("TUTEE_NAME");
        dateAndTime = getIntent().getStringExtra("DATE");

        Date d = new Date(dateAndTime);

        TextView torName = (TextView) findViewById(R.id.tutorName);
        torName.setText("Tutor: " +tutorName);

        TextView torEmail = (TextView) findViewById(R.id.tutorEmail);
        torEmail.setText("Tutor Email: " +tutorEmail);

        TextView teeName = (TextView) findViewById(R.id.tuteeName);
        teeName.setText("Tutee: " +tuteeName);

        TextView teeEmail = (TextView) findViewById(R.id.tuteeEmail);
        teeEmail.setText("Tutee Email: " +tuteeEmail);

        TextView date = (TextView) findViewById(R.id.date);
        date.setText("Date: " + d.getDateDescription());

        TextView time = (TextView) findViewById(R.id.time);
        time.setText("Time: " + d.getTimeString());
    }

    public void back(View v) {
        finish();
    }

    public void cancelAppt(View v) {
        handleApptCancelation();
    }

    private void handleApptCancelation() {
        // TODO: Chloe
    }
}
