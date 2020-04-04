package edu.upenn.cis350.cis350_finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import database_schema.User;

public class ProfileFragment extends Fragment {
    User user = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = RootView.findViewById(R.id.profile_name);
        TextView bio = RootView.findViewById(R.id.profile_bio);
        TextView school = RootView.findViewById(R.id.profile_school);
        TextView major = RootView.findViewById(R.id.profile_major);
        TextView gradYear = RootView.findViewById(R.id.profile_gradYear);
        TextView email = RootView.findViewById(R.id.profile_email);

        name.setText(user.getName());
        bio.setText(user.getBio());
        school.setText("School: " + user.getSchool());
        major.setText("Major: " + user.getMajor());
        gradYear.setText("Graduation Year: " + user.getGradYear());
        email.setText("Email: " + user.getEmail());
        return RootView;
    }

    public void setUser(User user) {
        this.user = user;
        Log.d("PROFILE", "frag:" + user.getName());
        //setUserFields();
    }
}
