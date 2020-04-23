package com.example.mycvtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SelectProfile extends AppCompatActivity {

    LinearLayout profilesLL;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_profiles);

        profilesLL = findViewById(R.id.profilesLL);
        inflater = LayoutInflater.from(this);
        loadProfiles();
    }

    @Override
    protected void onResume() {
        super.onResume();

        profilesLL = findViewById(R.id.profilesLL);
        inflater = LayoutInflater.from(this);
        loadProfiles();
    }


    public void loadProfiles () {

        DBHandler dbHandler = new DBHandler(this);
        Cursor profiles = dbHandler.getAllProfiles();

        if(profiles.getCount()==0) {
            return;
        }else {
            while (profiles.moveToNext()) {
                View profile = inflater.inflate(R.layout.profile_btn, profilesLL, false);
                final Button profileBtn = profile.findViewById(R.id.profileBtn);
                final String btnTitle = profiles.getString(1);
                // todo final String fname, ...
                profileBtn.setText(profiles.getString(1));
                profilesLL.addView(profile);
                profile.findViewById(R.id.profileBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("<-------------- profile is "+btnTitle);
                        selectProfile(btnTitle);
                    }
                });
            }
        }
    }

    public void selectProfile(String profileTitle){
        Intent i = new Intent(this, LoadSkill.class); // todo LoadProfile
        i.putExtra("profileTitle", profileTitle);
        startActivity(i);
    }

    public void addProfile(View view){
        Intent intent = new Intent(this, CreateProfile.class);
        startActivity(intent);
    }
}