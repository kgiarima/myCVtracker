package com.example.mycvtracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createProfile(View view) {
        Intent intent = new Intent(MainActivity.this, CreateProfile.class);
        startActivity(intent);
    }

    public void loadProfiles(View view) {
        Intent intent = new Intent(MainActivity.this, SelectProfile.class);
        startActivity(intent);
    }

    public void createSkill(View view) {
        Intent intent = new Intent(MainActivity.this, CreateSkill.class);
        startActivity(intent);
    }

    public void loadSkills(View view) {
        Intent intent = new Intent(MainActivity.this, SelectSkill.class);
        startActivity(intent);
    }

    public void showInfo(View view) {
        String info = "App created by:\n\nKiriakos Giarimagas\n\nMarios Tzitziras\n\nBetty Chrysou";
        showMessage("Info",info);
    }

    public void exitApp(View view) {
        finish();
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
