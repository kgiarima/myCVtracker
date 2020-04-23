package com.example.mycvtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectSkill extends AppCompatActivity {

    LinearLayout summaryLL, coreCompetenciesLL, technicalProficienciesLL, professionalExperienceLL, studiesLL, certificationsLL, otherLL;
    LayoutInflater inflater;

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

        loadSummaries("SUMMARY", summaryLL);
        loadSummaries("CORE COMPETENCIES", coreCompetenciesLL);
        loadSummaries("TECHNICAL PROFICIENCIES", technicalProficienciesLL);
        loadSummaries("PROFESSIONAL EXPERIENCE", professionalExperienceLL);
        loadSummaries("STUDIES", studiesLL);
        loadSummaries("CERTIFICATIONS", certificationsLL);
        loadSummaries("OTHER", otherLL);
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

        loadSummaries("SUMMARY", summaryLL);
        loadSummaries("CORE COMPETENCIES", coreCompetenciesLL);
        loadSummaries("TECHNICAL PROFICIENCIES", technicalProficienciesLL);
        loadSummaries("PROFESSIONAL EXPERIENCE", professionalExperienceLL);
        loadSummaries("STUDIES", studiesLL);
        loadSummaries("CERTIFICATIONS", certificationsLL);
        loadSummaries("OTHER", otherLL);
    }


    public void loadSummaries (String category, LinearLayout ll) {
        DBHandler dbHandler = new DBHandler(this);
        Cursor skills = dbHandler.findSkillsByCategory(category);

        if(skills.getCount()==0) {
            return;
        }else {
            while (skills.moveToNext()) {
                View sumView = inflater.inflate(R.layout.skill_btn, ll, false);
                final Button skillBtn = sumView.findViewById(R.id.skillBtn);
                final String btnTitle = skills.getString(1);
                skillBtn.setText(skills.getString(1));
                ll.addView(sumView);
                sumView.findViewById(R.id.skillBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("<-------------- skill is "+btnTitle);
                        selectSKill(btnTitle);
                     }
                });
            }
        }
    }

    public void selectSKill(String skillTitle){
//        finish();
        Intent i = new Intent(this, LoadSkill.class);
        i.putExtra("skillTitle", skillTitle);
        startActivity(i);
    }

    public void addSkill(View view){
//        finish();
        Intent intent = new Intent(SelectSkill.this, CreateSkill.class);
        startActivity(intent);
    }
}
