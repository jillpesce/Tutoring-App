package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import database_schema.Appointment;
import database_schema.Date;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class AppointmentActivity extends AppCompatActivity {
    String tutorEmail;
    String tutorName;
    String tuteeEmail;
    String tuteeName;
    String dateAndTime;
    User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        tutorEmail = getIntent().getStringExtra("TUTOR_EMAIL");
        tutorName = getIntent().getStringExtra("TUTOR_NAME");
        tuteeEmail = getIntent().getStringExtra("TUTEE_EMAIL");
        tuteeName = getIntent().getStringExtra("TUTEE_NAME");
        dateAndTime = getIntent().getStringExtra("DATE");
        String currUserEmail = getIntent().getStringExtra("CURR_EMAIL");
        currUser = (new RemoteDataSource().findUser(currUserEmail));

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
        RemoteDataSource rd = new RemoteDataSource();
        Appointment app = new Appointment(this.tutorName, this.tuteeName, this.dateAndTime,
                this.tutorEmail, this.tuteeEmail);
        String result = rd.cancelAppointment(app);
        if (result.equals("success")) {
            Toast.makeText(getApplicationContext(), "Appointment Cancelled",
                    Toast.LENGTH_LONG).show();
        }

        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}
