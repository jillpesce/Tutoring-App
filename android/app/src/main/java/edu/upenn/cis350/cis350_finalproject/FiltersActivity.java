package edu.upenn.cis350.cis350_finalproject;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import database_schema.Date;

public class FiltersActivity extends AppCompatActivity {
    String[] allCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        allCourses = ProfileFragment.allCourses();
    }

    public void back(View v) {
        finish();
    }

    public void clear(View v) {
        handleClearFilters();
    }

    private void handleClearFilters() {
        // TODO: Chloe
    }
}
