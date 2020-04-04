package edu.upenn.cis350.cis350_finalproject;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class TimeslotsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // need to pass TUTEE

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_timeslots);
        String tuteeUser = getIntent().getStringExtra("TUTEE");
    }

    public void onTimeslotSelected(View v) {
        EditText name = findViewById(R.id.name_edt);
    }

}
