package com.example.mycvtracker;

import java.util.ArrayList;

public class SelectedSkillsHandler {

    private static ArrayList<String> selectedSkills = new ArrayList<>();
    private static ArrayList<String> removedSkills = new ArrayList<>();
    private static ArrayList<String> totalSkills = new ArrayList<>();

    public static ArrayList<String> getTotalSkills() {
        return totalSkills;
    }

    public static void setTotalSkills(ArrayList<String> totalSkills) {
        SelectedSkillsHandler.totalSkills = totalSkills;
    }

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

    public static void clearArrayLists(){
        selectedSkills.clear();
        removedSkills.clear();
        totalSkills.clear();
    }

    public static void removeSkill(String skillTitle){
        selectedSkills.remove(skillTitle);
        removedSkills.add(skillTitle);
    }
}
