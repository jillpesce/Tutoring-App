package edu.upenn.cis350.cis350_finalproject;

import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import database_schema.User;
import datamanagement.RemoteDataSource;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    User user = null;
    String selection = null;
    View RootView = null;
    Boolean darkmode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = RootView.findViewById(R.id.profile_name);
        TextView bio = RootView.findViewById(R.id.profile_bio);
        TextView school = RootView.findViewById(R.id.profile_school);
        TextView major = RootView.findViewById(R.id.profile_major);
        TextView gradYear = RootView.findViewById(R.id.profile_gradYear);
        TextView email = RootView.findViewById(R.id.profile_email);
        TextView courses = RootView.findViewById(R.id.profile_courses);
        TextView toggle = RootView.findViewById(R.id.toggle_button);
        ImageView profilePic = RootView.findViewById(R.id.profile_pic);
        TextView darkmodeButton = RootView.findViewById(R.id.darkmodetoggle);

        Spinner spin = (Spinner) RootView.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> aa =
                new ArrayAdapter<CharSequence>(this.getActivity(), android.R.layout.simple_spinner_item, courseNames(allCourses()));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        Button button = (Button) RootView.findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(user.hasCourse(selection)){
                    Toast toast = Toast.makeText(getContext(), "Already chosen that class", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    user.addCourse(selection);
                    TextView courses = (TextView) RootView.findViewById(R.id.profile_courses);
                    courses.setText(parseCourses(user.getCourses()));

                    RemoteDataSource ds = new RemoteDataSource();
                    ds.addCourse(user);
                }
            }
        });

        name.setText(user.getName());
        bio.setText(user.getBio());
        school.setText("School: " + user.getSchool());
        major.setText("Major: " + user.getMajor());
        gradYear.setText("Graduation Year: " + user.getGradYear());
        email.setText("Email: " + user.getEmail());
        courses.setText("Courses: " + parseCourses(user.getCourses()));
        toggle.setText("Toggle to " + (user.getIsTutor() ? "Tutee" : "Tutor"));
        darkmodeButton.setText("ENABLE " + ((darkmode) ? "LIGHTMODE" : "DARKMODE"));
        if (user.getPicture() != null && !user.getPicture().isEmpty()) {
            String imageUrl = user.getPicture();
            Picasso.get().load(imageUrl).into(profilePic);
        } else {
            profilePic.setImageResource(R.drawable.ic_person_black_24dp);
        }
        return RootView;
    }


    private void refreshFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    public String parseCourses(ArrayList<String> courses){
        String result = "";
        for(String c : courses) {
            result += c + ", ";
        }
        return result;
    }

    public static String[] allCourses() {
        String[] allCourses = new String[]{
                new String("CIS 110"),
                new String("CIS 120"),
                new String("CIS 160"),
                new String("CIS 121"),
                new String("CIS 262"),
                new String("CIS 320")};
        return allCourses;
    }

    public String[] courseNames(String[] c) {
        //if we want to remove the course names that the tutor has already chosen
        /*
        String[] names = new String[c.length];
        for (int i = 0; i < allCourses().length; i++) {
            names[i] = allCourses()[i].getName();
        }
        String[] availableNames = new String[names.length-user.getCourses().size()];
        for (int i = 0; i < user.getCourses().size(); i++) {
            for (int j = 0; j < names.length; j++ ){
                if(user.getCourses().get(i).getName() == names[j]){
                    names[i] = "";
                }
            }
        }
        int pointer = 0;
        for (int i = 0; i < names.length; i++){
            if(names[i] != ""){
                availableNames[pointer] = names[i];
                pointer++;
            }
        }
        return availableNames;*/
        String[] names = new String[c.length];
        for (int i = 0; i < allCourses().length; i++) {
            names[i] = allCourses()[i];
        }
        return names;
    }


    public void setUser(User user) {
        this.user = user;
        Log.d("PROFILE", "frag:" + user.getName());
        //setUserFields();
    }

    public void setDarkmode(Boolean state) {
        darkmode = state;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selection = allCourses()[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}