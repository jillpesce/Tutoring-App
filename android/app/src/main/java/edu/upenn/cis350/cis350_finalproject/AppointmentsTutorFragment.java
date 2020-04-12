package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
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

        view = inflater.inflate(R.layout.fragment_tutee_appointments, container, false);

        RemoteDataSource ds = new RemoteDataSource();
        tutorAppts = ds.getTutorAppointments(user.getEmail());
        Collections.sort(tutorAppts);
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
            desc[counter] = d.getFullDescription() + " with " + a.getTutor();
            counter ++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, desc);

        lv = (ListView) view.findViewById(R.id.tutee_appointments);

        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, desc));
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Intent i = new Intent(getActivity(), AppointmentActivity.class);
        Appointment a = tutorAppts.get(position);
        i.putExtra("TUTEE_EMAIL", a.getTuteeEmail());
        i.putExtra("TUTEE_NAME", a.getTutee());
        i.putExtra("TUTOR_EMAIL", a.getTutorEmail());
        i.putExtra("TUTOR_NAME", a.getTutor());
        i.putExtra("DATE", a.getDate());
        getActivity().startActivity(i);
    }
}
