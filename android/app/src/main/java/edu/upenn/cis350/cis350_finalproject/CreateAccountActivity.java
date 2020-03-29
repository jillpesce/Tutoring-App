package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.appcompat.app.AppCompatActivity;

import database_schema.User;
import datamanagement.RemoteDataSource;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        String emailString = getIntent().getStringExtra("EMAIL");
        EditText email = (EditText) findViewById(R.id.email_edt);
        email.setText(emailString);
        Spinner spinner = findViewById(R.id.school_spinner);
        spinner.setOnItemSelectedListener(new SpinnerListener());
    }

    public class SpinnerListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String item = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    /**
     *
     * @param v
     * Called when submit button is pressed. Initiates request to create a profile.
     */
    public void onSubmitButtonClicked(View v) {
        EditText name = findViewById(R.id.name_edt);
        EditText email = findViewById(R.id.email_edt);
        //EditText school = findViewById(R.id.school_edt);
        Spinner school = findViewById(R.id.school_spinner);
        EditText graduationYear = findViewById(R.id.grad_edt);
        EditText major = findViewById(R.id.major_edt);
        EditText bio = findViewById(R.id.bio_edt);

        handleProfileCreation(name.getText().toString(), email.getText().toString(),
                school.getSelectedItem().toString(), graduationYear.getText().toString(),
                major.getText().toString(), bio.getText().toString());
    }

    /**
     *
     * @param name
     * @param email
     * @param school
     * @param gradYear
     * @param major
     * @param bio
     * This method should determine if all proper information was given to create a profile. If not,
     * a Toast should appear promoting the user to fill in all required information. If is it, then
     * it initiate a request to save the user to the database and start the Dashboard Activity.
     */
    private void handleProfileCreation(String name, String email, String school, String gradYear,
                                       String major, String bio) {
        if (emptyOrNull(name) || emptyOrNull(email) || emptyOrNull(school) || emptyOrNull(gradYear)
            || emptyOrNull(major)) {
            Toast.makeText(getApplicationContext(), "Please fill in all required fields",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (!validYearFormat(gradYear)) {
            Toast.makeText(getApplicationContext(),
                    "Graduation Year should be of the format: yyyy", Toast.LENGTH_LONG).show();
        } else {
            //save user to database
            RemoteDataSource ds = new RemoteDataSource();
            User u = new User(email, name, school, major, gradYear, bio);
            ds.saveUser(u);
            Intent intent = new Intent();
            intent.putExtra("USER", u);
            finish();
        }
    }

    /**
     *
     * @param s
     * @return true if the string is null or empty and false otherwise
     */
    private boolean emptyOrNull(String s) {
        if (s == null) {
            return true;
        }
        return s.isEmpty();
    }

    /**
     *
     * @param n
     * @return return true if the string is of the form 'yyyy' and false otherwise.
     */
    private boolean validYearFormat(String n) {
        if (n.length() != 4) {
            return false;
        } else {
            for (int i = 0; i < n.length(); i++) {
                char c = n.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }
    }
}
