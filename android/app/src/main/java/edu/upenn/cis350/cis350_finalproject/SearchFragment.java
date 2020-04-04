package edu.upenn.cis350.cis350_finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import database_schema.User;
import datamanagement.RemoteDataSource;

public class SearchFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Search", "in search fragment");
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SearchView searchView = (SearchView) getView().findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                RemoteDataSource ds = new RemoteDataSource();
                ArrayList<User> users = ds.searchUsers(query);
                TextView resultsView = (TextView) getView().findViewById(R.id.search_results);
                String resultsText = "";
                for(User user : users) {
                    resultsText += user.getName() +'\n';
                }
                resultsView.setText(resultsText);
                return true;
            }
        });
    }
}