package com.example.mycvtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoadSkill  extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String title;
    private String category;
    private String description;
    private String oldTitle;

    private EditText titleText;
    private Spinner categorySpinner;
    private EditText descriptionText;

    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_skill);

        titleText = findViewById(R.id.titleText);
        categorySpinner = findViewById(R.id.categorySpinner);
        descriptionText = findViewById(R.id.descriptionText);

        adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        loadSkill();
    }

    private void loadSkill() {
        Bundle extras = getIntent().getExtras();
        DBHandler dbHandler = new DBHandler(this);
        Skill skill = dbHandler.findSkill( extras.getString("skillTitle"));

        titleText.setText(skill.getTitle());
        oldTitle = titleText.getText().toString();
        categorySpinner.setSelection(adapter.getPosition(skill.getCategory()));
        descriptionText.setText(skill.getDescription());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        category = "SUMMARY";
    }

    public void updateSkill(View view) {
        DBHandler dbHandler = new DBHandler(this);
        title = titleText.getText().toString();
        category = categorySpinner.getSelectedItem().toString();
        description = descriptionText.getText().toString();

        try {
            boolean updated = dbHandler.updateSkill(oldTitle,title,category,description);
            if(updated) {
                Toast toast = Toast.makeText(this, "Skill was successfully updated!", Toast.LENGTH_SHORT);
                toast.show();

                finish();
            }else{
                Toast toast = Toast.makeText(this, "Skill could not be updated!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Error err){
            System.out.println("Error : "+err);
            Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void deleteSkill(View view) {
        DBHandler dbHandler = new DBHandler(this);
        try {
            boolean deleted = dbHandler.deleteSkill(oldTitle);
            if(deleted) {
                Toast toast = Toast.makeText(this, "Skill was successfully deleted!", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }else{
                Toast toast = Toast.makeText(this, "Skill could not be deleted!",Toast.LENGTH_SHORT);
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
}
