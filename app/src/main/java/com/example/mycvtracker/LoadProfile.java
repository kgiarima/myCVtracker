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

    String oldProfileTitle, profileTitle, fname, email, phone, birthday;
    LinearLayout summaryLL, coreCompetenciesLL, technicalProficienciesLL, professionalExperienceLL, studiesLL, certificationsLL, otherLL;
    LayoutInflater inflater;
    SelectedSkillsHandler ssh;
    private ArrayList<String> selectedSkills, savedSkills;
    private boolean stillOpen;

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

        selectedSkills = new ArrayList<>();
        savedSkills = new ArrayList<>();
        inflater = LayoutInflater.from(this);
        ssh = new SelectedSkillsHandler();
        stillOpen = false;

        loadProfile();
    }

    @Override
    protected void onResume(){
        super.onResume();

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

        stillOpen = false;
        System.out.println("<---------- Resumed");
//        selectedSkills = ssh.getSelectedSkills();
        loadProfileSkills();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(!stillOpen) {
            ssh.clearArrayList();
        }
    }


    private void setProfileSkills(String profileName){
        DBHandler dbHandler = new DBHandler(this);
        Cursor profileSkills = dbHandler.findSkillsByProfile(profileName);
        if(profileSkills.getCount()==0) {
            return;
        }else {
            while (profileSkills.moveToNext()) {
                savedSkills.add(profileSkills.getString(2));
                ssh.addSkill(profileSkills.getString(2));
            }
        }
    }

    private void loadProfileSkills(){
        DBHandler dbHandler = new DBHandler(this);
        for (String skillTitle:ssh.getSelectedSkills()){
            if(!selectedSkills.contains(skillTitle)&&!ssh.getRemovedSkills().contains(skillTitle)) {
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
                final View sumView = inflater.inflate(R.layout.skill_btn, categoryLL, false);
                final Button skillBtn = sumView.findViewById(R.id.skillBtn);
                final String btnTitle = skill.getTitle();
                skillBtn.setText(skill.getTitle());
                categoryLL.addView(sumView);
                selectedSkills.add(skill.getTitle());
                sumView.findViewById(R.id.skillBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectSkill(btnTitle);
                        System.out.println("<-------------- skill is " + btnTitle);
                    }
                });
            }
        }
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

        setProfileSkills(profile.getProfile());
        loadProfileSkills();
    }

    private void selectSkill(String title){
        stillOpen = true;
        Intent i = new Intent(this, LoadSkill.class);
        i.putExtra("skillTitle", title);
        i.putExtra("canBeRemoved",true);
        startActivity(i);
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
                setSkills(profileTitle);
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

    private void setSkills(String profileTitle) {
        DBHandler dbHandler = new DBHandler(this);
        Profile found = dbHandler.findProfile(profileTitle);
        if (found == null){
            Toast toast = Toast.makeText(this, "Profile could not be found!",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else{
            try {
                for(String skillTitle:selectedSkills) {
                    if(!savedSkills.contains(skillTitle)) {
                        boolean inserted = dbHandler.addSkillToProfile(skillTitle, profileTitle);
                        if (inserted) {
                            savedSkills.add(skillTitle);
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
        stillOpen = true;
        Intent intent = new Intent(LoadProfile.this, SelectSkill.class);
        intent.putExtra("returnSkill", true);
        startActivity(intent);
    }
}