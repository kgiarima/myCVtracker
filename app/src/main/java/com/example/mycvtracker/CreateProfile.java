package com.example.mycvtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

// CreateProfile handles the activity to create a new user profile
public class CreateProfile extends AppCompatActivity {

    private EditText profileTitleText, fnameText, emailText, phoneText, birthdayText;
    private LinearLayout summaryLL, coreCompetenciesLL, technicalProficienciesLL, professionalExperienceLL, studiesLL, certificationsLL, otherLL;
    private LayoutInflater inflater;
    private SelectedSkillsHandler ssh;
    private ArrayList<String> selectedSkills; // selected skills stores all the skills' titles that are selected to be added to the profile
    private boolean stillOpen; // stillOpen is used to determine whether the activity is still valid to keep the current info (profileName, fullName, etc) or reset them
    private static String profileName, fname, email, phone, birthday;

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
        coreCompetenciesLL = findViewById(R.id.coreCompetenciesLL);
        technicalProficienciesLL = findViewById(R.id.technicalProficienciesLL);
        professionalExperienceLL = findViewById(R.id.professionalExperienceLL);
        studiesLL = findViewById(R.id.studiesLL);
        certificationsLL = findViewById(R.id.certificationsLL);
        otherLL = findViewById(R.id.otherLL);

        inflater = LayoutInflater.from(this);
        ssh = new SelectedSkillsHandler();
        selectedSkills = new ArrayList<>();
        stillOpen = false;

        // initializes the string values of the profile
        updateValues();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.create_profile);

        profileTitleText = findViewById(R.id.profileName);
        fnameText = findViewById(R.id.fname);
        emailText = findViewById(R.id.email);
        phoneText = findViewById(R.id.phone);
        birthdayText = findViewById(R.id.birthday);

        profileTitleText.setText(profileName);
        fnameText.setText(fname);
        emailText.setText(email);
        phoneText.setText(phone);
        birthdayText.setText(birthday);

        summaryLL = findViewById(R.id.summaryLL);
        coreCompetenciesLL = findViewById(R.id.coreCompetenciesLL);
        technicalProficienciesLL = findViewById(R.id.technicalProficienciesLL);
        professionalExperienceLL = findViewById(R.id.professionalExperienceLL);
        studiesLL = findViewById(R.id.studiesLL);
        certificationsLL = findViewById(R.id.certificationsLL);
        otherLL = findViewById(R.id.otherLL);

        inflater = LayoutInflater.from(this);
        stillOpen = false;
        selectedSkills = ssh.getSelectedSkills();

        // show the skills to be added to the profile
        showSkills();
    }

    @Override
    protected void onStop(){
        super.onStop();
        // if activity is stopped not to add/remove skills to the profile the selectedSKills arrays should be cleared
        if(!stillOpen) {
            ssh.clearArrayLists();
        }
    }

    // add every selected skill to its category's linear layout
    private void showSkills(){
        DBHandler dbHandler = new DBHandler(this);
        if(selectedSkills.size()>0) {
            for(int i=0;i<selectedSkills.size();i++){
                Skill skill = dbHandler.findSkill(selectedSkills.get(i));
                LinearLayout categoryLL = summaryLL;
                switch (skill.getCategory()){
                    case("SUMMARY"):
                        categoryLL=summaryLL;
                        break;
                    case("CORE COMPETENCIES"):
                        categoryLL=coreCompetenciesLL;
                        break;
                    case("TECHNICAL PROFICIENCIES"):
                        categoryLL=technicalProficienciesLL;
                        break;
                    case("PROFESSIONAL EXPERIENCE"):
                        categoryLL=professionalExperienceLL;
                        break;
                    case("STUDIES"):
                        categoryLL=studiesLL;
                        break;
                    case("CERTIFICATIONS"):
                        categoryLL=certificationsLL;
                        break;
                    case("OTHER"):
                        categoryLL=otherLL;
                        break;
                }
                View sumView = inflater.inflate(R.layout.skill_btn, categoryLL, false);
                final Button skillBtn = sumView.findViewById(R.id.skillBtn);
                final String btnTitle = selectedSkills.get(i);
                skillBtn.setText(selectedSkills.get(i));
                categoryLL.addView(sumView);
                // on click of a skill Load that skill in a new activity
                sumView.findViewById(R.id.skillBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectSkill(btnTitle);
                    }
                });
            }
        }
    }

    // Load the selected skill in a new activity
    private void selectSkill(String title){
        stillOpen = true; // keep the current profile details
        updateValues(); // update the profile details to the latest user input
        Intent i = new Intent(this, LoadSkill.class);
        i.putExtra("skillTitle", title); // a skill can be retrieved from the db by its title
        i.putExtra("canBeRemoved",true); // skill can be removed from the current profile's selection
        startActivity(i);
    }

    // open skill selection to add new skills to the profile
    public void addSkill(View view) {
        stillOpen = true;
        updateValues();
        Intent intent = new Intent(CreateProfile.this, SelectSkill.class);
        intent.putExtra("returnSkill", true);
        startActivity(intent);
    }

    // update the profile details (profile name, full name, email...) to the latest user input
    private void updateValues(){
        profileName = profileTitleText.getText().toString();
        fname = fnameText.getText().toString();
        email = emailText.getText().toString();
        phone = phoneText.getText().toString();
        birthday = birthdayText.getText().toString();
    }

    // create the profile in the db and add all the related skills in the proper table
    public void createProfile(View view) {
        updateValues();
        if(profileName.isEmpty()){
            Toast toast = Toast.makeText(this, "Add a title first!",Toast.LENGTH_SHORT);
            toast.show();
        }else{
            DBHandler dbHandler = new DBHandler(this);
            Profile found = dbHandler.findProfile(profileName);
            if (found == null){
                try {
                    Profile profile = new Profile(profileName, fname, email, phone, birthday);
                    boolean inserted = dbHandler.addProfile(profile);
                    if(inserted) {
                        Toast toast = Toast.makeText(this, "Profile was successfully created!", Toast.LENGTH_SHORT);
                        toast.show();
                        setProfileSkills(profileName); // add all the related skills in the proper table
                        finish();
                    }else{
                        Toast toast = Toast.makeText(this, "Profile could not be created!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (Error err){
                    Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                Toast toast = Toast.makeText(this, "There is already a profile with that title!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // a profile's skills are saved in a different db table than the actual profile
    private void setProfileSkills(String profileTitle) {
        DBHandler dbHandler = new DBHandler(this);
        Profile found = dbHandler.findProfile(profileTitle);
        if (found == null){
            Toast toast = Toast.makeText(this, "Profile could not be found!",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else{
            try {
                // add every skill with the associated profile to the db
                for(String skillTitle:selectedSkills) {
                    boolean inserted = dbHandler.addSkillToProfile(skillTitle,profileTitle);
                    if (inserted) {

                    } else {
                        Toast toast = Toast.makeText(this, "Skill "+skillTitle+" could not be added to the profile!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }catch (Error err){
                System.out.println("Error : "+err);
                Toast toast = Toast.makeText(this, "Something went wrong adding the skills to profile!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void cancel(View view) {
        finish();
    }
}