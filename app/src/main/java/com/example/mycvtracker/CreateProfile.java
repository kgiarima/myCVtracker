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

public class CreateProfile extends AppCompatActivity {

    EditText profileTitleText, fnameText, emailText, phoneText, birthdayText;
    LinearLayout summaryLL, coreCompetenciesLL, technicalProficienciesLL, professionalExperienceLL, studiesLL, certificationsLL, otherLL;
    LayoutInflater inflater;
    private SelectedSkillsHandler ssh;
    private ArrayList<String> selectedSkills;
    private boolean stillOpen;
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

        updateValues();

        inflater = LayoutInflater.from(this);
        ssh = new SelectedSkillsHandler();
        selectedSkills = new ArrayList<>();

        stillOpen = false;
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
        System.out.println("<---------- Resumed");
        selectedSkills = ssh.getSelectedSkills();
        showSkills();
    }

    @Override
    protected void onStop(){
        System.out.println("<---------- profile closed");
        super.onStop();
        if(!stillOpen) {
            ssh.clearArrayLists();
        }
    }

    private void showSkills(){
        DBHandler dbHandler = new DBHandler(this);
        System.out.println("<---------- selectedSkills array size : "+selectedSkills.size());
        if(selectedSkills.size()==0) {
            return;
        }else {
            for(int i=0;i<selectedSkills.size();i++){
                System.out.println("<---------- skil "+i+" : "+selectedSkills.get(i));
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
                System.out.println("<---------- Categroy is : "+categoryLL);
                View sumView = inflater.inflate(R.layout.skill_btn, categoryLL, false);
                final Button skillBtn = sumView.findViewById(R.id.skillBtn);
                final String btnTitle = selectedSkills.get(i);
                skillBtn.setText(selectedSkills.get(i));
                System.out.println("<---------- Button text is : "+skillBtn.getText().toString());
                categoryLL.addView(sumView);
                sumView.findViewById(R.id.skillBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectSkill(btnTitle);
                        System.out.println("<-------------- skill is "+btnTitle);
                    }
                });
            }
        }
    }

    private void selectSkill(String title){
        updateValues();
        Intent i = new Intent(this, LoadSkill.class);
        i.putExtra("skillTitle", title);
        i.putExtra("canBeRemoved",true);
        stillOpen = true;
        startActivity(i);
    }

    public void addSkill(View view) {
        stillOpen = true;
        updateValues();
        Intent intent = new Intent(CreateProfile.this, SelectSkill.class);
        intent.putExtra("returnSkill", true);
        startActivity(intent);
    }

    private void updateValues(){
        profileName = profileTitleText.getText().toString();
        fname = fnameText.getText().toString();
        email = emailText.getText().toString();
        phone = phoneText.getText().toString();
        birthday = birthdayText.getText().toString();
    }

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
                        setProfileSkills(profileName);
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

    private void setProfileSkills(String profileTitle) {
        DBHandler dbHandler = new DBHandler(this);
        Profile found = dbHandler.findProfile(profileTitle);
        if (found == null){
            Toast toast = Toast.makeText(this, "Profile could not be found!",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else{
            try {
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