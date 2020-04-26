package com.example.mycvtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// LoadSkill handles the activity to list a selected skill with its properties, edit, delete or add/remove it to/from a profile if possible
public class LoadSkill  extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String title, category, description; // a skill's values
    private TextView titleText;
    private Spinner categorySpinner;
    private EditText descriptionText;
    private Button removeBtn;
    private SelectedSkillsHandler ssh;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_skill);

        titleText = findViewById(R.id.titleText);
        categorySpinner = findViewById(R.id.categorySpinner);
        descriptionText = findViewById(R.id.descriptionText);
        removeBtn = findViewById(R.id.removeBtn);
        ssh = new SelectedSkillsHandler();

        adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        loadSkill();
    }

    // load the selected skill
    private void loadSkill() {
        Bundle extras = getIntent().getExtras();
        DBHandler dbHandler = new DBHandler(this);
        Skill skill = dbHandler.findSkill( extras.getString("skillTitle"));
        if(skill!=null) {
            // if skill can be removed from a profile, make the remove button visible
            if(extras.getBoolean("canBeRemoved")){
                removeBtn.setVisibility(View.VISIBLE);
            }
            // load the skill's values
            titleText.setText(skill.getTitle());
            categorySpinner.setSelection(adapter.getPosition(skill.getCategory()));
            descriptionText.setText(skill.getDescription());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        category = "SUMMARY";
    }

    // update skill with the latest given values
    public void updateSkill(View view) {

        DBHandler dbHandler = new DBHandler(this);
        updateValues(); // update the skill's values with the latest user input

        try {
            boolean updated = dbHandler.updateSkill(title,category,description);
            if(updated) {
                Toast toast = Toast.makeText(this, "Skill was successfully updated!", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                Toast toast = Toast.makeText(this, "Skill could not be updated!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Error err){
            Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // update the skill's values with the latest user input
    private void updateValues() {
        title = titleText.getText().toString();
        category = categorySpinner.getSelectedItem().toString();
        description = descriptionText.getText().toString();
    }

    // delete the selected skill
    public void deleteSkill(View view) {
        DBHandler dbHandler = new DBHandler(this);
        try {
            boolean deleted = dbHandler.deleteSkill(title);
            if(deleted) {
                Toast toast = Toast.makeText(this, "Skill was successfully deleted!", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }else{
                Toast toast = Toast.makeText(this, "Skill could not be deleted!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Error err){
            Toast toast = Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // remove the selected skill from the profile we are creating / editing
    public void removeSkill(View view) {
        ssh.removeSkill(titleText.getText().toString());
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}