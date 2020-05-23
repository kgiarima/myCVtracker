package com.example.mycvtracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

// SelectProfile handles the activity to list all existing user profiles
public class SelectProfile extends AppCompatActivity {

    private LinearLayout profilesLL;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.load_profiles);

        profilesLL = findViewById(R.id.profilesLL);
        inflater = LayoutInflater.from(this);
        loadProfiles();
        setAnimation();
    }

    // enable background animation
    public void setAnimation(){
        ConstraintLayout cl = findViewById(R.id.layout);
        AnimationDrawable ad = (AnimationDrawable) cl.getBackground();
        ad.setEnterFadeDuration(1500);
        ad.setExitFadeDuration(3500);
        ad.start();
    }

    // retrieve all profiles from the db and list them in the profile's linear layout
    public void loadProfiles () {
        DBHandler dbHandler = new DBHandler(this);
        Cursor profiles = dbHandler.getAllProfiles();
        if(profiles.getCount()>0){
            while (profiles.moveToNext()) {
                View profile = inflater.inflate(R.layout.profile_btn, profilesLL, false);
                final Button profileBtn = profile.findViewById(R.id.profileBtn);
                final String btnTitle = profiles.getString(1);
                profileBtn.setText(profiles.getString(1));
                profilesLL.addView(profile);
                profile.findViewById(R.id.profileBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectProfile(btnTitle);
                    }
                });
            }
        }
    }

    // when selecting a profile (identified by its Profile name), Load it in a new activity
    public void selectProfile(String profileTitle){
        Intent i = new Intent(this, LoadProfile.class);
        i.putExtra("profileName", profileTitle); // profile name is used to retrieve a profile from the db
        startActivity(i);
    }

    // load the create new profile activity
    public void addProfile(View view){
        Intent intent = new Intent(this, CreateProfile.class);
        startActivity(intent);
    }

    public void cancel(View view){
        finish();
    }
}