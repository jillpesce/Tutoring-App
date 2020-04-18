package edu.upenn.cis350.cis350_finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import database_schema.Date;
import database_schema.Timeslot;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class FiltersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String[] allCourses;
    boolean[] selected;
    List<String> filteredCourses = null;
    String filteredTutor = null;
    String filteredTutorName = null;
    ListView lv;
    TextView tutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        allCourses = ProfileFragment.allCourses();
        selected = new boolean[allCourses.length];
        filteredCourses = new ArrayList<String>();

        tutors = (TextView) findViewById(R.id.selected_tutors);
        lv = (ListView) findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allCourses);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allCourses));
        lv.setOnItemClickListener(this);
    }

    public void back(View v) {

        for (int i = 0; i < allCourses.length; i++) {
            if(selected[i]) {
                filteredCourses.add(allCourses[i]);
            }
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Courses", filteredCourses.toArray(new String[0]));
        returnIntent.putExtra("TutorName", filteredTutorName);
        returnIntent.putExtra("Tutor", filteredTutor);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void clear(View v) {
        handleClearFilters();
    }

    private void handleClearFilters() {
        filteredTutor = null;
        tutors.setText("Tutor:");
        selected = new boolean[allCourses.length];
        for (int i = 0; i < allCourses.length; i++) {
            lv.getChildAt(i).setBackgroundColor(0x00000000);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (selected[position]) {
            lv.getChildAt(position).setBackgroundColor(0x00000000);
            selected[position] = false;
        } else {
            lv.getChildAt(position).setBackgroundColor(Color.GREEN);
            selected[position] = true;
        }
    }

    public void onSearch2(View v) {
        SearchView sv = (SearchView) findViewById(R.id.search_view_2);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                RemoteDataSource rd = new RemoteDataSource();
                User user = rd.findUserName(s);
                if (user != null) {
                    filteredTutor = user.getEmail();
                    filteredTutorName = user.getName();
                    tutors.setText("Tutor: " + user.getName());
                } else {
                    Toast.makeText(FiltersActivity.this, "No User Found.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
