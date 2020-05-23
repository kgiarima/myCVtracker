package com.example.mycvtracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

// SelectSkill handles the activity to list all existing skills to the user
public class SelectSkill extends AppCompatActivity {

    private LinearLayout summaryLL, coreCompetenciesLL, technicalProficienciesLL, professionalExperienceLL, studiesLL, certificationsLL, otherLL; // all skills' categories linear layouts
    private Button selectSkillBtn, editSkillBtn, selectedSkillBtn; // selectedSkillBtn is used to store the currently selected skill button
    private LayoutInflater inflater; // use inflater to add multiple skills in every category's linear layout
    private SelectedSkillsHandler ssh; // use to filter which skills should or shouldn't appear when adding skills to a profile
    private boolean returnSkill; // true when skills are expected to be selected (returned) to a profile
    private String selectedSkill; // selected skill's title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_skill);

        summaryLL = findViewById(R.id.summaryLL);
        coreCompetenciesLL = findViewById(R.id.coreCompetenciesLL);
        technicalProficienciesLL = findViewById(R.id.technicalProficienciesLL);
        professionalExperienceLL = findViewById(R.id.professionalExperienceLL);
        studiesLL = findViewById(R.id.studiesLL);
        certificationsLL = findViewById(R.id.certificationsLL);
        otherLL = findViewById(R.id.otherLL);
        inflater = LayoutInflater.from(this);
        selectSkillBtn = findViewById(R.id.selectSkillBtn);
        editSkillBtn = findViewById(R.id.editSkillBtn);
        ssh = new SelectedSkillsHandler();

        // add the appropriate skills for every skill category
        loadCategorySkills("SUMMARY", summaryLL);
        loadCategorySkills("CORE COMPETENCIES", coreCompetenciesLL);
        loadCategorySkills("TECHNICAL PROFICIENCIES", technicalProficienciesLL);
        loadCategorySkills("PROFESSIONAL EXPERIENCE", professionalExperienceLL);
        loadCategorySkills("STUDIES", studiesLL);
        loadCategorySkills("CERTIFICATIONS", certificationsLL);
        loadCategorySkills("OTHER", otherLL);

        Bundle extras = getIntent().getExtras();
        returnSkill =  extras.getBoolean("returnSkill"); // returnSkill value is passed from the previous activity to determine if a skill is expected to be added / returned to a profile
        setAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.select_skill);

        summaryLL = findViewById(R.id.summaryLL);
        coreCompetenciesLL = findViewById(R.id.coreCompetenciesLL);
        technicalProficienciesLL = findViewById(R.id.technicalProficienciesLL);
        professionalExperienceLL = findViewById(R.id.professionalExperienceLL);
        studiesLL = findViewById(R.id.studiesLL);
        certificationsLL = findViewById(R.id.certificationsLL);
        otherLL = findViewById(R.id.otherLL);
        inflater = LayoutInflater.from(this);
        selectSkillBtn = findViewById(R.id.selectSkillBtn);
        editSkillBtn = findViewById(R.id.editSkillBtn);
        ssh = new SelectedSkillsHandler();

        // add the appropriate skills for every skill category
        loadCategorySkills("SUMMARY", summaryLL);
        loadCategorySkills("CORE COMPETENCIES", coreCompetenciesLL);
        loadCategorySkills("TECHNICAL PROFICIENCIES", technicalProficienciesLL);
        loadCategorySkills("PROFESSIONAL EXPERIENCE", professionalExperienceLL);
        loadCategorySkills("STUDIES", studiesLL);
        loadCategorySkills("CERTIFICATIONS", certificationsLL);
        loadCategorySkills("OTHER", otherLL);

        Bundle extras = getIntent().getExtras();
        returnSkill =  extras.getBoolean("returnSkill"); // returnSkill value is passed from the previous activity to determine if a skill is expected to be added / returned to a profile
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

    // Load every valid skill grouped by it's category
    public void loadCategorySkills(String category, LinearLayout ll) {
        DBHandler dbHandler = new DBHandler(this);
        // find all category's skills from the db
        Cursor skills = dbHandler.findSkillsByCategory(category);

        if(skills.getCount()>0) {
            while (skills.moveToNext()) {
                /*
                if a skill can be returned/added to a profile and it's not already present in the profile's activity
                or skills are not expected to be returned/added to a skill
                create the skill's button inside the activity
                */
                if(!returnSkill||(returnSkill&&!ssh.getTotalSkills().contains(skills.getString(1)))) {
                    /*
                    inflate the category's linear layout with the skill view (Button inside) template
                    set the skill's title (unique) as the view's text to identify
                    and finally add the view to the linear layout
                     */
                    final View sumView = inflater.inflate(R.layout.skill_btn, ll, false);
                    final Button skillBtn = sumView.findViewById(R.id.skillBtn);
                    final String btnTitle = skills.getString(1);
                    skillBtn.setText(skills.getString(1));
                    ll.addView(sumView);
                    /*
                    On the click of a Skill (view)
                    if a skill can be returned /added to a profile, set the select/add and edit buttons visible. Make Toast to show what skill is selected
                    if not open the selected Skill in the LoadSkill activity to view / edit
                     */
                    sumView.findViewById(R.id.skillBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (returnSkill) {
                                Toast.makeText(SelectSkill.this,"Skill : "+btnTitle+" is selected",Toast.LENGTH_SHORT).show();
                                selectedSkill = btnTitle;
                                selectedSkillBtn = skillBtn;
                                selectSkillBtn.setVisibility(View.VISIBLE);
                                editSkillBtn.setVisibility(View.VISIBLE);
                            } else {
                                selectSkill(btnTitle);
                            }
                        }
                    });
                }
            }
        }
    }

    // Open the selected Skill in the LoadSkill activity to view / edit
    public void selectSkill(String skillTitle){
        Intent i = new Intent(this, LoadSkill.class);
        i.putExtra("skillTitle", skillTitle); // skillTitle will be used in the LoadSkill activity to retrieve the skill from the db
        startActivity(i);
    }

    // Create a new skill
    public void addSkill(View view){
        Intent intent = new Intent(SelectSkill.this, CreateSkill.class);
        startActivity(intent);
    }

    /*
     add a skill to the ssh to be used by the profile activity later
     remove the skill's view from the current activity
     set the select and edit buttons invisible again
     */
    public void addSkillToProfile(View view) {
        ssh.addSkill(selectedSkill);
        ViewGroup parentView = (ViewGroup) selectedSkillBtn.getParent();
        parentView.removeView(selectedSkillBtn);

        selectSkillBtn.setVisibility(View.INVISIBLE);
        editSkillBtn.setVisibility(View.INVISIBLE);

        Toast.makeText(this,"Skill : "+selectedSkill+" successfully added",Toast.LENGTH_SHORT).show();
    }

    // on click of the edit button, load the selected skill to be edited
    public void editSkill(View view) {
        selectSkill(selectedSkill);
    }

    public void exit(View view) {
        finish();
    }
}
