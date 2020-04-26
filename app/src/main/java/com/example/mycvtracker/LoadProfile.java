package com.example.mycvtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoadProfile extends AppCompatActivity{

    EditText fnameText, emailText, phoneText, birthdayText;
    TextView profileTitleText;

    private static String oldProfileTitle, profileTitle, fname, email, phone, birthday;
    LinearLayout summaryLL, coreCompetenciesLL, technicalProficienciesLL, professionalExperienceLL, studiesLL, certificationsLL, otherLL;
    LayoutInflater inflater;
    SelectedSkillsHandler ssh;
    private ArrayList<String> totalSkills, dbSkills, addedSkills, removedSkills;
    private boolean keepSSHArrays;
    private boolean returned = false;

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

        System.out.println("<---------- OnCreate : ");

        loadProfile();
        setTotalSKills();
        showProfileSkills();

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
            System.out.println("<---------- Resumed, totalSKills are: " + totalSkills);


            loadProfile();
            setTotalSKills();
            showProfileSkills();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(!keepSSHArrays) {
            ssh.clearArrayLists();
        }
    }

    // add skills titles to dbSkills (loadProfile)
    private void loadProfileSkills(String profileName){
        DBHandler dbHandler = new DBHandler(this);
        Cursor profileSkills = dbHandler.findSkillsByProfile(profileName);
        if(profileSkills.getCount()==0) {
            return;
        }else {
            while (profileSkills.moveToNext()) {
                dbSkills.add(profileSkills.getString(2));
//                ssh.addSkill(profileSkills.getString(2));
            }
            System.out.println("<---------- loaded dbSkills are: "+dbSkills);
        }
    }

    private void setTotalSKills(){
        System.out.println("<---------- Removed Skills are: "+removedSkills);
        for(String dbSkill:dbSkills){
            if(!removedSkills.contains(dbSkill)){
                totalSkills.add(dbSkill);
            }
        }
        System.out.println("<----------  added skills are: "+addedSkills);
        for(String newSkill:addedSkills){
            if(!removedSkills.contains(newSkill)){
                totalSkills.add(newSkill);
            }
        }
        System.out.println("<---------- after setting, totalSKills are: "+totalSkills);
    }


    private void showProfileSkills(){
        DBHandler dbHandler = new DBHandler(this);
        // fore every selected (not saved) skill
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
                        System.out.println("<-------------- skill is " + btnTitle);
                    }
                });
        }
    }

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

    private void selectSkill(String title){
        updateValues();
        keepSSHArrays = true;
        returned = true;
        Intent i = new Intent(this, LoadSkill.class);
        i.putExtra("skillTitle", title);
        i.putExtra("canBeRemoved",true);
        startActivity(i);
    }

    public void updateProfile(View view) {
        DBHandler dbHandler = new DBHandler(this);
        updateValues();

        try {
            boolean updated = dbHandler.updateProfile(profileTitle,fname, email,phone,birthday);
            if(updated) {
                Toast toast = Toast.makeText(this, "Profile was successfully updated!", Toast.LENGTH_SHORT);
                toast.show();
                setSkills(profileTitle);
                removeSkills(profileTitle);
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

    private void removeSkills(String profileName){
        DBHandler dbHandler = new DBHandler(this);
        Profile found = dbHandler.findProfile(profileName);
        if (found == null){
            Toast toast = Toast.makeText(this, "Profile could not be found!",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else {
            try {
                for (String skillTitle : removedSkills) {
                    if(dbSkills.contains(skillTitle)){
                        System.out.println("Removing skill "+skillTitle);
                        boolean skillRemoved = dbHandler.removeSkillFromProfile(profileName,skillTitle);
                        System.out.println("Removing skill was "+skillRemoved);
                    }
                }
            } catch (Error err) {
                System.out.println("Error : " + err);
                Toast toast = Toast.makeText(this, "Something went wrong adding the skills to profile!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

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
                System.out.println("Error : "+err);
                Toast toast = Toast.makeText(this, "Something went wrong adding the skills to profile!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    public void deleteProfile(View view) {
        DBHandler dbHandler = new DBHandler(this);
        try {
            boolean deleted = dbHandler.deleteProfile(oldProfileTitle);
            if(deleted) {
                Toast toast = Toast.makeText(this, "Profile was successfully deleted!", Toast.LENGTH_SHORT);
                dbHandler.removeSkillsFromProfile(oldProfileTitle);
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

    private void updateValues(){
        profileTitle = profileTitleText.getText().toString();
        fname = fnameText.getText().toString();
        email = emailText.getText().toString();
        phone = phoneText.getText().toString();
        birthday = birthdayText.getText().toString();
    }

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