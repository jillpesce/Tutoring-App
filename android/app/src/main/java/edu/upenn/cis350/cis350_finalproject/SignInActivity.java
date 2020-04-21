package edu.upenn.cis350.cis350_finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import database_schema.User;
import datamanagement.RemoteDataSource;

public class SignInActivity extends AppCompatActivity {
    private GoogleSignInClient client;
    public static final int RC_SIGN_IN = 1;
    public static final int DashboardActivity_ID = 2;
    public static final int CreateAccountActivity_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        this.client = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        Log.d("SIGN IN", "\n\n\n SIGN IN BUTTON CLICK \n\n\n\n");
                        Intent signInIntent = client.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            //user has already signed in
            // go to the user's dashboard
            Intent dashboardIntent = new Intent(this, DashboardActivity.class);
            dashboardIntent.putExtra("EMAIL", account.getEmail());
            startActivityForResult(dashboardIntent, DashboardActivity_ID);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (requestCode == DashboardActivity_ID) {
            signOut();
        } else if (requestCode == CreateAccountActivity_ID) {
            Intent dashboardIntent = new Intent(this, DashboardActivity.class);
            User user = (User) dashboardIntent.getSerializableExtra("USER");
            Log.d("EMAIL", user.getEmail());
            Log.d("CHECKPOINT", "herree!!!!!!");
            dashboardIntent.putExtra("EMAIL", user.getEmail());
        }
    }

    /**
     * Uses the Google SignIn Account to see if the user has an account. If the user is already
     * registered, it launches the Dashboard Activity. Else, it launches the CreateAccountActivity
     * @param completedTask
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            RemoteDataSource ds = new RemoteDataSource();
            User user = ds.findUser(email);
            if (user != null) {
                // the user already has an account - go to dashboard
                Intent dashboardIntent = new Intent(this, DashboardActivity.class);
                dashboardIntent.putExtra("EMAIL", email);
                startActivityForResult(dashboardIntent, DashboardActivity_ID);
            } else {
                // the user does not already have an account - go to create account
                Intent createAccountIntent = new Intent(this,
                        CreateAccountActivity.class);
                createAccountIntent.putExtra("EMAIL", email);
                startActivityForResult(createAccountIntent, CreateAccountActivity_ID);
            }
        } catch (ApiException e) {
            Log.w("FAILURE", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), "Login failed. Please try again.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void signOut() {
       client.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        // ...
                    }
                });
    }
}
