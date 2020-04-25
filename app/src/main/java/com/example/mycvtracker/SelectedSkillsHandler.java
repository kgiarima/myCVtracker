package com.example.mycvtracker;

import java.util.ArrayList;

public class SelectedSkillsHandler {

    private static ArrayList<String> selectedSkills = new ArrayList<>();
    private static ArrayList<String> removedSkills = new ArrayList<>();

    public SelectedSkillsHandler(){}

    public static ArrayList<String> getSelectedSkills() {
        return selectedSkills;
    }

    public static void setSelectedSkills(ArrayList<String> selectedSkills) {
        SelectedSkillsHandler.selectedSkills = selectedSkills;
    }

    public static void addSkill(String skillTitle){
        removedSkills.remove(skillTitle);
        selectedSkills.add(skillTitle);
    }

    public static ArrayList<String> getRemovedSkills() {
        return removedSkills;
    }

    public static void setRemovedSkills(ArrayList<String> removedSkills) {
        SelectedSkillsHandler.removedSkills = removedSkills;
    }

    public static void clearArrayList(){
        selectedSkills.clear();
        removedSkills.clear();
    }

    public static void removeSkill(String skillTitle){
        selectedSkills.remove(skillTitle);
        removedSkills.add(skillTitle);
    }
}
