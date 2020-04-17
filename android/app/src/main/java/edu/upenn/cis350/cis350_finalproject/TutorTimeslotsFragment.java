package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database_schema.Date;
import database_schema.Timeslot;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class TutorTimeslotsFragment extends Fragment {
    ListView lv;
    View view;
//    Timeslot[] ts;
    List<Timeslot> setTimeslots;
    String tuteeUser;
//    String[] timeslotStrings = [];
    User user = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // need to pass TUTEE
        // String tuteeUser = getIntent().getStringExtra("TUTEE");

        view = inflater.inflate(R.layout.fragment_tutor_view_timeslots, container, false);

        Button button = (Button) view.findViewById(R.id.new_time);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewTimeslotActivity.class);
                i.putExtra("EMAIL", user.getEmail());
                i.putExtra("NAME", user.getName());
                i.putExtra("COURSES", new String[0]); // need to change this
                getActivity().startActivity(i);
            }
        });

        RemoteDataSource ds = new RemoteDataSource();
        setTimeslots = ds.getTutorTimeslots(user.getEmail());

        return view;
    }

    public void setUser(User user) {
        this.user = user;
        //setUserFields();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (setTimeslots != null) {
            Collections.sort(setTimeslots);
        } else {
            setTimeslots = new ArrayList<>();
        }

        String[] desc = new String[setTimeslots.size()];
        int counter = 0;
        for (Timeslot t : setTimeslots) {
            Date d = new Date(t.getDate());
            desc[counter] = d.getFullDescription() + " with " + t.getTutorName();
            counter ++;
        }

//        String[] timeslots = {"March 18, 2020 at 6:00PM", "March 18, 2020 at 6:30PM", "March 18, 2020 at 7:00PM", "March 18, 2020 at 7:30PM"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, desc);

        lv = (ListView) view.findViewById(R.id.timeslots);

        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, desc));
//        lv.setOnItemClickListener(this);
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//        Intent i = new Intent(getActivity(), RequestAppointmentActivity.class);
//        Timeslot t = setTimeslots.get(position);
//        i.putExtra("TUTEE_EMAIL", user.getEmail());
//        i.putExtra("TUTEE_NAME", user.getName());
//        i.putExtra("TUTOR_EMAIL", t.getTutor());
//        i.putExtra("TUTOR_NAME", t.getTutorName());
//        i.putExtra("DATE", t.getDate());
//        getActivity().startActivity(i);
//    }


}
