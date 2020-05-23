package com.example.mycvtracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

// LoadProfile handles the activity to list a selected profile with its properties and edit or delete it
public class LoadProfile extends AppCompatActivity{

    private EditText fnameText, emailText, phoneText, birthdayText;
    private TextView profileTitleText;
    private static String profileTitle, fname, email, phone, birthday; // a profile's values
    private LinearLayout summaryLL, coreCompetenciesLL, technicalProficienciesLL, professionalExperienceLL, studiesLL, certificationsLL, otherLL;
    private LayoutInflater inflater;
    private SelectedSkillsHandler ssh;
    private ArrayList<String> totalSkills, dbSkills, addedSkills, removedSkills; // total skills are the skills stored in the db + the new skills added - the skills removed
    private boolean keepSSHArrays; // use to determine if selectedSkills arrays should be reset (when exiting the activity) or not
    private boolean returned = false; // on activity creation 'onResume' is also called so use this to know if the activity has been truly on resume

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
        coreCompetenciesLL = findViewById(R.id.coreCompetenciesLL);
        technicalProficienciesLL = findViewById(R.id.technicalProficienciesLL);
        professionalExperienceLL = findViewById(R.id.professionalExperienceLL);
        studiesLL = findViewById(R.id.studiesLL);
        certificationsLL = findViewById(R.id.certificationsLL);
        otherLL = findViewById(R.id.otherLL);

        totalSkills = new ArrayList<>();
        dbSkills = new ArrayList<>();
        addedSkills = new ArrayList<>();
        removedSkills = new ArrayList<>();

        inflater = LayoutInflater.from(this);
        ssh = new SelectedSkillsHandler();
        // use to clear (when exiting loadProfile) or not, the skills arrayLists
        keepSSHArrays = false;

        loadProfile(); // load the profile with its values
        setTotalSKills(); // load and set total skills as the sum of : the skills stored in the db + the new skills added - the skills removed
        showProfileSkills(); // show the total active skills to the user
        setAnimation();
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(returned) {
            setContentView(R.layout.load_profile);

            profileTitleText = findViewById(R.id.profileName);
            fnameText = findViewById(R.id.fname);
            emailText = findViewById(R.id.email);
            phoneText = findViewById(R.id.phone);
            birthdayText = findViewById(R.id.birthday);

            // set the profile's values to the latest user input
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

            totalSkills = new ArrayList<>();
            dbSkills = new ArrayList<>();
            addedSkills = ssh.getSelectedSkills();
            removedSkills = ssh.getRemovedSkills();

            keepSSHArrays = false;

            loadProfile(); // load the profile with its values
            setTotalSKills(); // load and set total skills as the sum of : the skills stored in the db + the new skills added - the skills removed
            showProfileSkills(); // show the total active skills to the user
            setAnimation();
        }
    }

    // enable background animation
    public void setAnimation(){
        ConstraintLayout cl = findViewById(R.id.layout);
        AnimationDrawable ad = (AnimationDrawable) cl.getBackground();
        ad.setEnterFadeDuration(1500);
        ad.setExitFadeDuration(3500);
        ad.start();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(!keepSSHArrays) {
            ssh.clearArrayLists();
        }
    }

    // load profile's skills from the db and add them to dbSkills
    private void loadProfileSkills(String profileName){
        DBHandler dbHandler = new DBHandler(this);
        Cursor profileSkills = dbHandler.findSkillsByProfile(profileName);
        if(profileSkills.getCount()>0) {
            while (profileSkills.moveToNext()) {
                dbSkills.add(profileSkills.getString(2));
            }
        }
    }

    // add all the not removed, new or saved skills to totalSkills to display as active
    private void setTotalSKills(){
        for(String dbSkill:dbSkills){
            if(!removedSkills.contains(dbSkill)){
                totalSkills.add(dbSkill);
            }
        }
        for(String newSkill:addedSkills){
            if(!removedSkills.contains(newSkill)){
                totalSkills.add(newSkill);
            }
        }
    }

    // show all active skills to the user (totalSkills)
    private void showProfileSkills(){
        DBHandler dbHandler = new DBHandler(this);
        for (String skillTitle:totalSkills){
                Skill skill = dbHandler.findSkill(skillTitle);
                LinearLayout categoryLL = summaryLL;
                switch (skill.getCategory()) {
                    case ("SUMMARY"):
                        categoryLL = summaryLL;
                        break;
                    case ("CORE COMPETENCIES"):
                        categoryLL = coreCompetenciesLL;
                        break;
                    case ("TECHNICAL PROFICIENCIES"):
                        categoryLL = technicalProficienciesLL;
                        break;
                    case ("PROFESSIONAL EXPERIENCE"):
                        categoryLL = professionalExperienceLL;
                        break;
                    case ("STUDIES"):
                        categoryLL = studiesLL;
                        break;
                    case ("CERTIFICATIONS"):
                        categoryLL = certificationsLL;
                        break;
                    case ("OTHER"):
                        categoryLL = otherLL;
                        break;
                }
                // add skill to the activity
                final View sumView = inflater.inflate(R.layout.skill_btn, categoryLL, false);
                final Button skillBtn = sumView.findViewById(R.id.skillBtn);
                final String btnTitle = skill.getTitle();
                skillBtn.setText(skill.getTitle());
                categoryLL.addView(sumView);
                // add skill to shown skills
                sumView.findViewById(R.id.skillBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectSkill(btnTitle);
                    }
                });
        }
    }

    // retrieve the profile from the db using its profile name
    private void loadProfile() {
        Bundle extras = getIntent().getExtras();
        DBHandler dbHandler = new DBHandler(this);
        Profile profile = dbHandler.findProfile( extras.getString("profileName"));

        profileTitleText.setText(profile.getProfile());
        if(!returned) {
            fnameText.setText(profile.getFname());
            emailText.setText(profile.getEmail());
            phoneText.setText(profile.getPhone());
            birthdayText.setText(profile.getBirthday());
        }
        loadProfileSkills(profile.getProfile());
    }

    // load a selected skill to view, edit, remove or delete
    private void selectSkill(String title){
        updateValues(); // update current profile's values to latest user input
        keepSSHArrays = true;
        returned = true;
        Intent i = new Intent(this, LoadSkill.class);
        i.putExtra("skillTitle", title);
        i.putExtra("canBeRemoved",true);
        startActivity(i);
    }

    // update the profile's values and associated skills
    public void updateProfile(View view) {
        DBHandler dbHandler = new DBHandler(this);
        updateValues();

        try {
            boolean updated = dbHandler.updateProfile(profileTitle,fname, email,phone,birthday);
            if(updated) {
                Toast toast = Toast.makeText(this, "Profile was successfully updated!", Toast.LENGTH_SHORT);
                toast.show();
                setSkills(profileTitle); // update the db with the new profile-skills relations
                removeSkills(profileTitle); // remove selected skills from the db
            }else{
                Toast toast = Toast.makeText(this, "Profile could not be updated!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Error err){
            Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // remove selected skills from the db
    private void removeSkills(String profileName){
        DBHandler dbHandler = new DBHandler(this);
        Profile found = dbHandler.findProfile(profileName);
        if (found == null){
            Toast toast = Toast.makeText(this, "Profile could not be found. Skills could not be added!", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            try {
                for (String skillTitle : removedSkills) {
                    if(dbSkills.contains(skillTitle)){
                        dbHandler.removeSkillFromProfile(profileName,skillTitle);
                    }
                }
            } catch (Error err) {
                Toast toast = Toast.makeText(this, "Something went wrong adding the skills to profile!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // update the db with the new profile-skills relations
    private void setSkills(String profileTitle) {
        DBHandler dbHandler = new DBHandler(this);
        Profile found = dbHandler.findProfile(profileTitle);
        if (found == null){
            Toast toast = Toast.makeText(this, "Profile could not be found!",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else{
            try {
                for(String skillTitle:addedSkills) {
                    if(!dbSkills.contains(skillTitle)&&!removedSkills.contains(skillTitle)) {
                        boolean inserted = dbHandler.addSkillToProfile(skillTitle, profileTitle);
                        // if skill is successfully added to the db move it from addedSkills to dbSkills
                        if (inserted) {
                            dbSkills.add(skillTitle);
                            addedSkills.remove(skillTitle);
                        } else {
                            Toast toast = Toast.makeText(this, "Skill " + skillTitle + " could not be added to the profile!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            }catch (Error err){
                Toast toast = Toast.makeText(this, "Something went wrong adding the skills to profile!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // delete a profile and all associated profile - skills relations
    public void deleteProfile(View view) {
        DBHandler dbHandler = new DBHandler(this);
        profileTitle = profileTitleText.getText().toString();
        try {
            // delete profile
            boolean deleted = dbHandler.deleteProfile(profileTitle);
            if(deleted) {
                Toast.makeText(this, "Profile was successfully deleted!", Toast.LENGTH_SHORT).show();
                // delete associated profile - skills relations
                for (String skillTitle : dbSkills) {
                    System.out.println("Removing skill "+skillTitle);
                    dbHandler.removeSkillFromProfile(profileTitle,skillTitle);
                }
                finish();
            }else{
                Toast toast = Toast.makeText(this, "Profile could not be deleted!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Error err){
            Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // update profile's values to the latest user input
    private void updateValues(){
        profileTitle = profileTitleText.getText().toString();
        fname = fnameText.getText().toString();
        email = emailText.getText().toString();
        phone = phoneText.getText().toString();
        birthday = birthdayText.getText().toString();
    }

    // open the selectSkill activity to add new skills to the profile
    public void addSkill(View view) {
        updateValues();
        ssh.setSelectedSkills(addedSkills);
        ssh.setTotalSkills(totalSkills);
        keepSSHArrays = true;
        returned = true;
        Intent intent = new Intent(LoadProfile.this, SelectSkill.class);
        intent.putExtra("returnSkill", true);
        startActivity(intent);
    }

    public void cancel(View view) {
        finish();
    }
}