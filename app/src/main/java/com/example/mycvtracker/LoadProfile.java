package com.example.mycvtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoadProfile extends AppCompatActivity{

    EditText profileTitleText, fnameText, emailText, phoneText, birthdayText;
    String oldProfileTitle, profileTitle, fname, email, phone, birthday;
    LinearLayout summaryLL;
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_profile);

        profileTitleText = findViewById(R.id.profileName);
        fnameText = findViewById(R.id.fname);
        emailText = findViewById(R.id.email);
        phoneText = findViewById(R.id.phone);
        birthdayText = findViewById(R.id.birthday);

        summaryLL = findViewById(R.id.summaryLL);
        inflater = LayoutInflater.from(this);

        loadProfile();
    }

    private void loadProfile() {
        Bundle extras = getIntent().getExtras();
        DBHandler dbHandler = new DBHandler(this);
        Profile profile = dbHandler.findProfile( extras.getString("profileName"));

        profileTitleText.setText(profile.getProfile());
        oldProfileTitle = profileTitleText.getText().toString();
        fnameText.setText(profile.getFname());
        emailText.setText(profile.getEmail());
        phoneText.setText(profile.getPhone());
        birthdayText.setText(profile.getBirthday());
    }

    public void updateProfile(View view) {
        DBHandler dbHandler = new DBHandler(this);

        profileTitle = profileTitleText.getText().toString();
        fname = fnameText.getText().toString();
        email = emailText.getText().toString();
        phone = phoneText.getText().toString();
        birthday = birthdayText.getText().toString();

        try {
            boolean updated = dbHandler.updateProfile(oldProfileTitle,profileTitle,fname, email,phone,birthday);
            if(updated) {
                Toast toast = Toast.makeText(this, "Profile was successfully updated!", Toast.LENGTH_SHORT);
                toast.show();

                finish();
            }else{
                Toast toast = Toast.makeText(this, "Profile could not be updated!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Error err){
            System.out.println("Error : "+err);
            Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void deleteProfile(View view) {
        DBHandler dbHandler = new DBHandler(this);
        try {
            boolean deleted = dbHandler.deleteProfile(oldProfileTitle);
            if(deleted) {
                Toast toast = Toast.makeText(this, "Profile was successfully deleted!", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }else{
                Toast toast = Toast.makeText(this, "Profile could not be deleted!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Error err){
            System.out.println("Error : "+err);
            Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void cancel(View view) {
        finish();
    }


    public void addSkill(View view) {

    }
}
