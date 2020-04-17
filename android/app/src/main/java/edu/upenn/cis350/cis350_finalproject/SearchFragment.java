package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import database_schema.Appointment;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {
    private List<User> users;
    SearchView searchView;
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] userNames;
    static final int OtherUserProfile_ID = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Search", "in search fragment");
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        RemoteDataSource rd = new RemoteDataSource();
        users = rd.getAllUsers();
        if (users != null) {
            Collections.sort(users);
        } else {
            users = new ArrayList<User>();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userNames = new String[users.size()];
        int counter = 0;
        for (User u : users) {
            userNames[counter] = u.getName() + " : " + u.getEmail();
            counter++;
        }

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, userNames);
        listView = getView().findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        searchView = (SearchView) getView().findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
        String text = adapter.getItem(position);
        String email = text.substring(text.indexOf(":") + 2);
        intent.putExtra("PROFILE USER", email);
        getActivity().startActivityForResult(intent, OtherUserProfile_ID);
    }

}