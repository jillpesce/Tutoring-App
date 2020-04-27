package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import database_schema.Appointment;
import database_schema.Date;
import database_schema.User;
import datamanagement.RemoteDataSource;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AppointmentActivity extends AppCompatActivity {
    String tutorEmail;
    String tutorName;
    String tuteeEmail;
    String tuteeName;
    String dateAndTime;
    User currUser;
    Boolean confirmed;
    Appointment a;
    Date d;
    Boolean isTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        tutorEmail = getIntent().getStringExtra("TUTOR_EMAIL");
        tutorName = getIntent().getStringExtra("TUTOR_NAME");
        tuteeEmail = getIntent().getStringExtra("TUTEE_EMAIL");
        tuteeName = getIntent().getStringExtra("TUTEE_NAME");
        dateAndTime = getIntent().getStringExtra("DATE");
        confirmed = getIntent().getBooleanExtra("CONFIRMED", false);
        String currUserEmail = getIntent().getStringExtra("CURR_EMAIL");
        currUser = (new RemoteDataSource().findUser(currUserEmail));
        isTutor = getIntent().getBooleanExtra("ISTUTOR", false);
        a = (Appointment) getIntent().getSerializableExtra("APPOINTMENT");
        d = new Date(dateAndTime);

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

        Button button =(Button)findViewById(R.id.acceptAppt);
        if (confirmed || currUserEmail.equals(tuteeEmail)){
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
        }


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

    public void acceptAppt(View v) {
        handleApptAcceptance();
    }

    private void handleApptAcceptance() {
        RemoteDataSource rd = new RemoteDataSource();
        Log.i("YUH", a.getTutorEmail() + " " + a.getTuteeEmail() + " " + a.getDate() + " " + a.getConfirmed());
        String result = rd.acceptAppointment(a);
        a.confirmAppointment();
        if (result.equals("success")) {
            Toast.makeText(getApplicationContext(), "Appointment Accepted",
                    Toast.LENGTH_LONG).show();
        }

        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }

    public void addtoCalendar(View v) {
        handleAddEvent();
    }

    private void handleAddEvent() {
        GregorianCalendar calDate = new GregorianCalendar(d.getYearInt(), d.getMonthInt(), d.getDayInt(), d.getHourInt(), 0);

        Calendar calendarEvent = Calendar.getInstance();
        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");
        if (isTutor) {
            i.putExtra(CalendarContract.Events.TITLE, "Tutoring appointment with " +tuteeName);
        } else {
            i.putExtra(CalendarContract.Events.TITLE, "Tutoring appointment with " +tutorName);
        }
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis() + 60 * 60 * 1000);
        startActivity(i);
    }

}
