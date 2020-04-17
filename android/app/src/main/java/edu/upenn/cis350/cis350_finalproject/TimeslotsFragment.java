package edu.upenn.cis350.cis350_finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import database_schema.Date;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class TimeslotsFragment extends Fragment implements OnItemClickListener {
    ListView lv;
    View view;
    List<Timeslot> setTimeslots;
    String[] filteredTutors = new String[0];
    String[] filteredCourses = new String[0];
    String tuteeUser;
    User user = null;
    int FILTER_ACTIVITY = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // need to pass TUTEE
        // String tuteeUser = getIntent().getStringExtra("TUTEE");

        view = inflater.inflate(R.layout.fragment_view_timeslots, container, false);

        Button button = (Button) view.findViewById(R.id.filter_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FiltersActivity.class);
                getActivity().startActivityForResult(i, FILTER_ACTIVITY);
            }
        });

        RemoteDataSource ds = new RemoteDataSource();
        setTimeslots = ds.getAllTimeslots();

        if (setTimeslots != null) {
            Collections.sort(setTimeslots);
        } else {
            setTimeslots = new ArrayList<>();
        }
        return view;
    }

    public void setUser(User user) {
        this.user = user;
        //setUserFields();
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILTER_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                filteredCourses=data.getStringArrayExtra("Courses");
                filteredTutors=data.getStringArrayExtra("Tutors");
            }
        }
     }

     private void filterTimeslots() {
        List<String> fTutors = Arrays.asList(filteredTutors);
        List<String> fCourses = Arrays.asList(filteredCourses);

        for (Timeslot t: setTimeslots) {
            Boolean removed = false;
            List<String> tCourses = Arrays.asList(t.getCourses());

            // only check if there are selected tutors.
            if (fTutors.size() != 0) {
                String tutorName = t.getTutorName();
                if (!fTutors.contains(tutorName)) {
                    setTimeslots.remove(t);
                    removed = true;
                }
            }

            // has not been removed in tutor filtering process.
            if (!removed) {
                Boolean foundCourseMatch = false;
                for(String s1 : tCourses) {
                    for (String s2 : fCourses) {
                        if (s1.equals(s2)) {
                            foundCourseMatch = true;
                        }
                    }
                }

                if (!foundCourseMatch) {
                    setTimeslots.remove(t);
                }
            }
        }
     }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Timeslot t1 = new Timeslot("snie@seas.upenn.edu", "2020041310", new String[0],"Selina Nie" );
//        Timeslot t2 = new Timeslot("juliechn@seas.upenn.edu", "2020041210", new String[0],"Julie Chen" );
//
//        ts = new Timeslot[2];
//        ts[0] = t1;
//        ts[1] = t2;
//
//        setTimeslots = new ArrayList<Timeslot>(Arrays.asList(ts));

//        Collections.sort(setTimeslots);

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
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Intent i = new Intent(getActivity(), RequestAppointmentActivity.class);
        Timeslot t = setTimeslots.get(position);
        i.putExtra("TUTEE_EMAIL", user.getEmail());
        i.putExtra("TUTEE_NAME", user.getName());
        i.putExtra("TUTOR_EMAIL", t.getTutor());
        i.putExtra("TUTOR_NAME", t.getTutorName());
        i.putExtra("DATE", t.getDate());
        getActivity().startActivity(i);
    }


}
