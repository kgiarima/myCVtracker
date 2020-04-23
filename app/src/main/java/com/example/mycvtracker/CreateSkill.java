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

public class CreateSkill  extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private String title;
    private String category;
    private String description;

    private EditText titleText;
    private Spinner categorySpinner;
    private EditText descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_skill);

        titleText = findViewById(R.id.titleText);
        categorySpinner = findViewById(R.id.categorySpinner);
        descriptionText = findViewById(R.id.descriptionText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
    }

    public void saveSkill(View view){
        title = titleText.getText().toString();
        category = categorySpinner.getSelectedItem().toString();
        description = descriptionText.getText().toString();

        if(title.isEmpty()){
            Toast toast = Toast.makeText(this, "Add a title first!",Toast.LENGTH_SHORT);
            toast.show();
        }else if(description.isEmpty()){
            Toast toast = Toast.makeText(this, "Add a description first!",Toast.LENGTH_SHORT);
            toast.show();
        }else{
            DBHandler dbHandler = new DBHandler(this);
            Skill found = dbHandler.findSkill(title);
            if (found == null){
                try {
                    Skill skill = new Skill(title, category, description);
                    boolean inserted = dbHandler.addSkill(skill);
                    if(inserted) {
                        titleText.setText("");
                        descriptionText.setText("");
                        Toast toast = Toast.makeText(this, "Skill was successfully created!", Toast.LENGTH_SHORT);
                        toast.show();

                        finish();
                    }else{
                        Toast toast = Toast.makeText(this, "Skill could not be created!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (Error err){
                    System.out.println("Error : "+err);
                    Toast toast = Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                Toast toast = Toast.makeText(this, "There is already a skill with that title!",Toast.LENGTH_SHORT);
                toast.show();
            }
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

    public void cancel(View view) {
        finish();
    }
}