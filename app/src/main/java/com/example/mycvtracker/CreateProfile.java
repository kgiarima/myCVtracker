package com.example.mycvtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CreateProfile extends AppCompatActivity {

    EditText profileTitleText, fnameText, emailText, phoneText, birthdayText;
    LinearLayout summaryLL;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);

        profileTitleText = findViewById(R.id.profileName);
        fnameText = findViewById(R.id.fname);
        emailText = findViewById(R.id.email);
        phoneText = findViewById(R.id.phone);
        birthdayText = findViewById(R.id.birthday);

        summaryLL = findViewById(R.id.summaryLL);
        inflater = LayoutInflater.from(this);
    }

    public void addSummary(View view) {
        finish();
        Intent intent = new Intent(CreateProfile.this, SelectSkill.class);
        startActivity(intent);

//        for(int i=0;i<5;i++) {
//            View sumView = inflater.inflate(R.layout.skill_btn, summaryLL, false);
//            Button skillBtn = sumView.findViewById(R.id.skillBtn);
//            skillBtn.setText("Summary "+i);
//
//            summaryLL.addView(sumView);
//        }
    }

    public void clickOnSkill(View view){

    }

    public void addSkill(View view) {
        Intent intent = new Intent(CreateProfile.this, SelectSkill.class);
        startActivity(intent);
    }

    public void createProfile(View view) {
        String profileTitle = profileTitleText.getText().toString();
        String fname = fnameText.getText().toString();
        String email = emailText.getText().toString();
        String phone = phoneText.getText().toString();
        String birthday = birthdayText.getText().toString();

        if(profileTitle.isEmpty()){
            Toast toast = Toast.makeText(this, "Add a title first!",Toast.LENGTH_SHORT);
            toast.show();
        }else{
            DBHandler dbHandler = new DBHandler(this);
            Profile found = dbHandler.findProfile(profileTitle);
            if (found == null){
                try {
                    Profile profile = new Profile(profileTitle, fname, email, phone, birthday);
                    boolean inserted = dbHandler.addProfile(profile);
                    if(inserted) {
                        Toast toast = Toast.makeText(this, "Profile was successfully created!", Toast.LENGTH_SHORT);
                        toast.show();

                        finish();
                    }else{
                        Toast toast = Toast.makeText(this, "Profile could not be created!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (Error err){
                    System.out.println("Error : "+err);
                    Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                Toast toast = Toast.makeText(this, "There is already a profile with that title!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void cancel(View view) {
        finish();
    }
}
