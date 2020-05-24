package com.example.mycvtracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    // create a new profile template
    public void createProfile(View view) {
        Intent intent = new Intent(MainActivity.this, CreateProfile.class);
        startActivity(intent);
    }

    // load existing profile templates
    public void loadProfiles(View view) {
        Intent intent = new Intent(MainActivity.this, SelectProfile.class);
        startActivity(intent);
    }

    // create a new skill
    public void createSkill(View view) {
        Intent intent = new Intent(MainActivity.this, CreateSkill.class);
        startActivity(intent);
    }

    // load existing skills
    public void loadSkills(View view) {
        Intent intent = new Intent(MainActivity.this, SelectSkill.class);
        intent.putExtra("returnSkill", false);
        startActivity(intent);
    }

    // show some info about the app
    public void showInfo(View view) {
        String message = "Create your own personal CV templates based on your unique skills \n\n\nAn application created by:\nKiriakos Giarimagas\nPanagiota Xrysou\nMarios Tzitziras";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Application Info");
        builder.setMessage(message);
        builder.show();
    }

    public void exitApp(View view) {
        finish();
    }
}