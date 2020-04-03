package edu.upenn.cis350.cis350_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.*;

import androidx.fragment.app.Fragment;
import database_schema.Timeslot;
import database_schema.Tutor;
import database_schema.User;

public class TimeslotsFragment extends Fragment {
    List<Timeslot> ts;
    String tuteeUser;
    User user = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // need to pass TUTEE
        // String tuteeUser = getIntent().getStringExtra("TUTEE");

        View RootView = inflater.inflate(R.layout.fragment_view_timeslots, container, false);
//        User u = new User("snie@seas.upenn.edu", "Selina Nie", "SEAS", "CIS", "2021", "we out here bois");
//        Tutor a = new Tutor(u);
//        Date d = new Date();
//        String[] courses = {"CIS 121", "NETS 150"};
//        ts.add(new Timeslot(a, d, courses));
//        populateTimeslots();
        return RootView;
    }

    public void setUser(User user) {
        this.user = user;
        //setUserFields();
    }

//    public void onTimeslotSelected(View v) {
//        EditText name = findViewById(R.id.name_edt);
//    }

//    private void populateTimeslots() {
//        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.listLayout);
//        LayoutInflater li =  (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for (int i = 0; i < ts.size();  i++){
//            View tempView = li.inflate(R.layout.fragment_view_timeslots, null);
//            TextView textMain = (TextView) tempView.findViewById(R.id.timeslot_text);
//            textMain.setText("" + ts.get(i).getDate());
//
//            TextView textTutor = (TextView) tempView.findViewById(R.id.timeslot_detail);
//            textTutor.setText(ts.get(i).getTutor().getName() +" tutoring " +ts.get(i).getCourses());
//
//            mainLayout.addView(tempView);
//        }
//    }

}
