package edu.upenn.cis350.cis350_finalproject;

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

public class TimeslotsFragment extends Fragment implements OnItemClickListener {
    ListView lv;
    View view;
    Timeslot[] ts;
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

        view = inflater.inflate(R.layout.fragment_view_timeslots, container, false);

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

        return view;

//        User u = new User("snie@seas.upenn.edu", "Selina Nie", "SEAS", "CIS", "2021", "we out here bois");
//        Tutor a = new Tutor(u);
//        Date d = new Date();
//        String[] courses = {"CIS 121", "NETS 150"};
//        ts.add(new Timeslot(a, d, courses));
//
//            String[] timeslots = {"March 18, 2020 at 6:00PM", "March 18, 2020 at 6:30PM", "March 18, 2020 at 7:00PM", "March 18, 2020 at 7:30PM"};
//
//            ArrayList<String> ts = new ArrayList<String>(Arrays.asList(timeslots));
//            ArrayAdapter<String> tsAdapter = new ArrayAdapter<String>(getActivity(),3, ts); //strings to be diplayed
//
//            ListView listView = (ListView) RootView.findViewById(R.id.timeslots);
//            listView.setAdapter(tsAdapter);
//
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


//        view = RootView;
    }

    public void setUser(User user) {
        this.user = user;
        //setUserFields();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Timeslot t1 = new Timeslot("snie@seas.upenn.edu", "2020041310", new String[0],"Selina Nie" );
        Timeslot t2 = new Timeslot("juliechn@seas.upenn.edu", "2020041210", new String[0],"Julie Chen" );

        ts = new Timeslot[2];
        ts[0] = t1;
        ts[1] = t2;

        setTimeslots = new ArrayList<Timeslot>(Arrays.asList(ts));
        Collections.sort(setTimeslots);

        String[] desc = new String[ts.length];
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
