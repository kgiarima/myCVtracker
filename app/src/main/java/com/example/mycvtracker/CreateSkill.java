package com.example.mycvtracker;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

// CreateSkill handles the activity of creating a new skill
public class CreateSkill  extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    // title, category, and description of the new skill
    private String title,category,description;
    private EditText titleText, descriptionText;
    private Spinner categorySpinner;
    private ArrayAdapter<CharSequence> adapter; // use array adapter to handle the spinner's category selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_skill);

        titleText = findViewById(R.id.titleText);
        categorySpinner = findViewById(R.id.categorySpinner);
        descriptionText = findViewById(R.id.descriptionText);

        // the 'categories' resource array holds all available skill categories
        adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        category = "SUMMARY";
    } // set the default category to 'summary'

    // create the new skill using the current values
    public void createSkill(View view){
        updateSkillValues();

        // title is mandatory
        if(title.isEmpty()){
            Toast toast = Toast.makeText(this, "Add a title first!",Toast.LENGTH_SHORT);
            toast.show();
        }else{
            DBHandler dbHandler = new DBHandler(this);
            // check if there is an existing skill by the same title
            Skill skillFound = dbHandler.findSkill(title);
            // if there is not already a skill with the same title create the skill, else show appropriate message
            if (skillFound == null){
                try {
                    Skill skill = new Skill(title, category, description);
                    // add skill to the db
                    boolean inserted = dbHandler.addSkill(skill);
                    if(inserted) {
                        resetValues();
                        Toast toast = Toast.makeText(this, "Skill was successfully created!", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(this, "Skill could not be created!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (Error err){
                    Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                Toast toast = Toast.makeText(this, "There is already a skill with that title!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // set skill values to default
    private void resetValues() {
        titleText.setText("");
        categorySpinner.setSelection(adapter.getPosition("SUMMARY"));
        descriptionText.setText("");
    }

    // set skill values to current ones
    private void updateSkillValues() {
        title = titleText.getText().toString();
        category = categorySpinner.getSelectedItem().toString();
        description = descriptionText.getText().toString();
    }

    public void cancel(View view) {
        finish();
    }
}