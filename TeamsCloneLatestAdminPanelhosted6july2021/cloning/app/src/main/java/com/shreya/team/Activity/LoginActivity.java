package com.shreya.team.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import com.shreya.team.R;
import com.shreya.team.Retrofit.Api;
import com.shreya.team.Utill.AppSharedPreferences;
import com.shreya.team.Utill.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//Code for activity to performed when the user wants to login into the app
public class LoginActivity extends AppCompatActivity {
    //Declaring components and GoogleSignInClient
    SignInButton siginbutton;
    private static final int RC_SIGN_IN = 234;

    GoogleSignInClient mGoogleSignInClient;
    //function to be performed on commencement of the Login Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Tools.getTheme(this);
        //basic layout setup
        setContentView(R.layout.activity_login);
       //Google sign in with token from Firebase
        siginbutton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //Login to the app on click of sign in if google account credentials are valid
        siginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    //Get google account details for displaying on the settings page and set status of that particular account as logged in
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String name = account.getDisplayName();
                String email = account.getEmail();
                String img_url = String.valueOf(account.getPhotoUrl());

                DatasendtoDatabase(name,email,img_url);
                AppSharedPreferences.setLoggedIn(getApplicationContext(), true,name,email,img_url);
            } catch (ApiException | IOException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    //defining the DatasendtoDatabase - if credentials valid, then launch Main Activity else, display error message in logcat
    private void DatasendtoDatabase(String name, String email, String img_url) throws IOException {
        (Api.createAPI().doCreateUserWithField(name,email,img_url)).enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Intent i=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("responseseror", String.valueOf(t));
            }


        });

    }
   //authenticate with google sign in client
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onStart() {
        super.onStart();


    }

}