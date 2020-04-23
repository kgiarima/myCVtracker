package com.example.mycvtracker;

public class Profile  {

    private int id;
    private String profile;
    private String fname;
    private String email;
    private String phone;
    private String birthday;

    public Profile(String profile, String fullName,String email,String phone,String birthday){
        this.profile = profile;
        this.fname = fullName;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    public Profile(){

    }

    public String getFname() { return fname; }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
