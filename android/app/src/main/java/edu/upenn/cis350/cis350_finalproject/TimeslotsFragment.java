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

import com.mongodb.client.model.Filters;

import database_schema.User;
import datamanagement.RemoteDataSource;

public class TimeslotsFragment extends Fragment implements OnItemClickListener {
    ListView lv;
    View view;
    List<Timeslot> setTimeslots;
    String filteredTutor = null;
    String[] filteredCourses = null;
    String tuteeUser;
    String filteredTutorName;
    User user = null;
    int FILTER_ACTIVITY = 5;
    ArrayAdapter<String> adapter;
    RemoteDataSource ds = new RemoteDataSource();
    TextView filters;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // need to pass TUTEE
        // String tuteeUser = getIntent().getStringExtra("TUTEE");

        view = inflater.inflate(R.layout.fragment_view_timeslots, container, false);

        Button button = (Button) view.findViewById(R.id.filter_button);
        filters = (TextView) view.findViewById(R.id.filter);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] c = new String[]{"CIS 160", "CIS 120"};
                Intent i = new Intent(getActivity(), FiltersActivity.class);
                startActivityForResult(i, FILTER_ACTIVITY);
//                filteredTutor = null;
//                filteredCourses = c;
//                populateList(null, c);
            }
        });

        return populateList(filteredTutor, filteredCourses);
    }

    private View populateList(String one, String[] two) {
        String fi = "Filters: ";
        if (filteredTutorName != null) {
            fi = fi + filteredTutorName + ", ";
        }

        if (two != null) {
            for (String s : two) {
                fi = fi + s + ", ";
            }
        }
        filters.setText(fi);

        if (one != null) {
            Log.d("tutor user:", one);
        } else {
            Log.d("tutor user:", "null");
        }

        if (two != null) {
            Log.d("classes size:", "" + two.length);
        } else {
            Log.d("classes size:", "null");
        }
        setTimeslots = ds.getFilteredTimeslots(one, two);

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

        lv = (ListView) view.findViewById(R.id.timeslots);
        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, desc));
        lv.setOnItemClickListener(this);

        return view;
    }

    public void setUser(User user) {
        this.user = user;
        //setUserFields();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_ACTIVITY && resultCode == Activity.RESULT_OK){
            Log.d("INTENT OK", "yuuuuhh");
            filteredCourses=data.getStringArrayExtra("Courses");
            if (filteredCourses.length == 0) {
                filteredCourses = null;
            }
            filteredTutorName=data.getStringExtra("TutorName");
            filteredTutor=data.getStringExtra("Tutor");
            populateList(filteredTutor,filteredCourses);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        String[] desc = new String[setTimeslots.size()];
//        int counter = 0;
//        for (Timeslot t : setTimeslots) {
//            Date d = new Date(t.getDate());
//            desc[counter] = d.getFullDescription() + " with " + t.getTutorName();
//            counter ++;
//        }

//        String[] timeslots = {"March 18, 2020 at 6:00PM", "March 18, 2020 at 6:30PM", "March 18, 2020 at 7:00PM", "March 18, 2020 at 7:30PM"};

//        lv = (ListView) view.findViewById(R.id.timeslots);
//        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, desc));
//        lv.setOnItemClickListener(this);
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