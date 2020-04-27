package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database_schema.Appointment;
import database_schema.Date;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class AppointmentsTutorFragment extends Fragment implements OnItemClickListener {
    ListView lv;
    View view;
//    Timeslot[] ts;
    List<Appointment> tutorAppts;
    String tuteeUser;
//    String[] timeslotStrings = [];
    User user = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tutor_appointments, container, false);

        RemoteDataSource ds = new RemoteDataSource();
        tutorAppts = ds.getTutorAppointments(user.getEmail());
        if (tutorAppts != null) {
            Collections.sort(tutorAppts);
        } else {
            tutorAppts = new ArrayList<Appointment>();
        }
        return view;
    }

    public void setUser(User user) {
        this.user = user;
        //setUserFields();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] desc = new String[tutorAppts.size()];
        int counter = 0;
        for (Appointment a : tutorAppts) {
            Date d = new Date(a.getDate());
            if(a.getConfirmed()) {
                desc[counter] = d.getFullDescription() + " with " + a.getTutee() + " ---- CONFIRMED";
            } else {
                desc[counter] = d.getFullDescription() + " with " + a.getTutee() + " ---- UNCONFIRMED" ;
            }
            counter ++;
        }

        lv = (ListView) view.findViewById(R.id.tutor_appointments);
        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, desc));
        lv.setOnItemClickListener(this);

        //handleColoringConfirmed();
    }

    //this function colors an appointment RED if it needs to be confirmed
    private void handleColoringConfirmed () {
        while(lv.getLastVisiblePosition() < 1);
        for (int i = 0; i < lv.getCount(); i++) {
            if (!tutorAppts.get(i).getConfirmed()){
                lv.getChildAt(i).setBackgroundColor(Color.RED);
            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Intent i = new Intent(getActivity(), AppointmentActivity.class);
        Appointment a = tutorAppts.get(position);
        i.putExtra("APPOINTMENT", a);
        i.putExtra("TUTEE_EMAIL", a.getTuteeEmail());
        i.putExtra("TUTEE_NAME", a.getTutee());
        i.putExtra("TUTOR_EMAIL", a.getTutorEmail());
        i.putExtra("TUTOR_NAME", a.getTutor());
        i.putExtra("DATE", a.getDate());
        i.putExtra("CURR_EMAIL", user.getEmail());
        i.putExtra("CONFIRMED", a.getConfirmed());
        i.putExtra("ISTUTOR", true);
        getActivity().startActivityForResult(i, 2);
    }
}
