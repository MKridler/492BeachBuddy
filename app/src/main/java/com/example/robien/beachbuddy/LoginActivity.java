package com.example.robien.beachbuddy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;


// ADD SEARCH TO MENU NEXT TO SETTINGS DOTS LATER
public class LoginActivity extends AppCompatActivity {


    private CallbackManager callbackManager;

    private TextView info;
    private LoginButton loginButton;

    private TextView email;
    private TextView gender;
    private TextView facebookName;
    private LinearLayout infoLayout;
    private LinearLayout relLayout;
    private ProfilePictureView profilePictureView;
    private Button classButt;
    private Button searchButt;
    private String sEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login_layout);
        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        email = (TextView)findViewById(R.id.email);
        facebookName = (TextView)findViewById(R.id.name);
        gender = (TextView)findViewById(R.id.gender);
        infoLayout = (LinearLayout)findViewById(R.id.layout_info);
        relLayout = (LinearLayout)findViewById(R.id.layout_info1);
        profilePictureView = (ProfilePictureView)findViewById(R.id.image);
        classButt = (Button)findViewById(R.id.classButton);
        searchButt = (Button)findViewById(R.id.searchButton);
        //loginButton.setReadPermissions(Arrays.asList("public_profile"));
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {


                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                setProfileToView(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
    }

            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {

                info.setText("Login attempt failed.");
            }
        });








    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setProfileToView(JSONObject jsonObject) {
        try {
            email.setText(jsonObject.getString("email"));


            sEmail = email.getText().toString();

            gender.setText(jsonObject.getString("gender"));
            facebookName.setText(jsonObject.getString("name"));

            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            profilePictureView.setProfileId(jsonObject.getString("id"));
            relLayout.setVisibility(View.INVISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
            classButt.setVisibility(View.VISIBLE);
            searchButt.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }}




    public void userReg(View view) {

        startActivity(new Intent(this, RegPersonInfoActivity.class));
    }

    public void classReg(View view){
        Log.v("sEmail", "sEmail is: " +  sEmail);
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(sEmail);

        startActivity(new Intent(this, RegClassActivity.class));
    }

    public void classNav(View view) {
        Intent goToMainPageIntent = new Intent(this,NavigationActivity.class);
        startActivity(goToMainPageIntent);
    }





}