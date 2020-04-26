package com.example.mycvtracker;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "cvTrackerDB.db";

    public static final String TABLE_SKILLS = "skills";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String TABLE_PROFILES = "profiles";
    public static final String COLUMN_PID = "_id";
    public static final String COLUMN_PROFILE = "profile";
    public static final String COLUMN_FNAME = "fname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_BIRTHDAY = "birthday";

    public static final String TABLE_PROFILESKILLS = "profileskills";
    public static final String COLUMN_PS_ID = "_id";
    public static final String COLUMN_PS_PROFILE = "profile";
    public static final String COLUMN_PS_SKILL = "skill"; // skill = skill title

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_SKILLS_TABLE = "CREATE TABLE " + TABLE_SKILLS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_SKILLS_TABLE);

        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
                + COLUMN_PID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PROFILE + " TEXT,"
                + COLUMN_FNAME + " TEXT,"
                + COLUMN_EMAIL  + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_BIRTHDAY + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_PROFILES_TABLE);

        String CREATE_PROFILESKILLS_TABLE = "CREATE TABLE " + TABLE_PROFILESKILLS + "("
                + COLUMN_PS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PS_PROFILE + " TEXT,"
                + COLUMN_PS_SKILL + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_PROFILESKILLS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SKILLS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILESKILLS);
        onCreate(sqLiteDatabase);
    }

    public boolean addSkillToProfile(String skillTitle, String profileName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PS_SKILL, skillTitle);
        values.put(COLUMN_PS_PROFILE, profileName);
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.insert(TABLE_PROFILESKILLS, null, values);
        if(res==-1) {
            return false;
        }else{
            return true;
        }
    }
    public Cursor findSkillsByProfile(String profileName){
        String query = "SELECT * FROM " + TABLE_PROFILESKILLS + " WHERE " +COLUMN_PS_PROFILE + " = '" + profileName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


    public boolean removeSkillFromProfile(String profileName, String title) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_PROFILESKILLS + " WHERE " + COLUMN_PS_PROFILE + " = '" + profileName + "' AND "+COLUMN_PS_SKILL +" = '"+title+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            System.out.println("Removing skill found with id "+cursor.getString(0));
            int deleted = db.delete(TABLE_PROFILESKILLS, COLUMN_PS_ID + " = ?", new String[] { cursor.getString(0) });
            if(deleted>0){
                result = true;
            }
        }
        cursor.close();
        return result;
    }

    public void removeSkillsFromProfile(String profileName) {
        boolean result = false;
        String query = "DELETE * FROM " + TABLE_PROFILESKILLS + " WHERE " + COLUMN_PS_PROFILE + " = '" + profileName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(query, null);
        return;
    }



    public boolean addSkill(Skill skill) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, skill.getTitle());
        values.put(COLUMN_CATEGORY, skill.getCategory());
        values.put(COLUMN_DESCRIPTION, skill.getDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.insert(TABLE_SKILLS, null, values);
        db.close();
        if(res==-1) {
            return false;
        }else{
            return true;
        }
    }

    public Skill findSkill(String title) {
        String query = "SELECT * FROM " + TABLE_SKILLS + " WHERE " + COLUMN_TITLE+ " = '" + title + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Skill skill = new Skill();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            skill.setId(Integer.parseInt(cursor.getString(0)));
            skill.setTitle(cursor.getString(1));
            skill.setCategory(cursor.getString(2));
            skill.setDescription(cursor.getString(3));
            cursor.close();
        } else {
            skill = null;
        }
        return skill;
    }

    public Cursor findSkillsByCategory(String category){
        String query = "SELECT * FROM " + TABLE_SKILLS + " WHERE " + COLUMN_CATEGORY+ " = '" + category + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


    public boolean deleteSkill(String title) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_SKILLS + " WHERE " + COLUMN_TITLE + " = '" + title + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int deleted = db.delete(TABLE_SKILLS, COLUMN_ID + " = ?", new String[] { cursor.getString(0) });
            if(deleted>0){
                result = true;
            }
        }
        cursor.close();
        return result;
    }

    public boolean updateSkill(String oldTitle, String title, String category, String description) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_SKILLS + " WHERE " + COLUMN_TITLE + " = '" + oldTitle + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID, Integer.parseInt(cursor.getString(0)));
            cv.put(COLUMN_TITLE, title);
            cv.put(COLUMN_CATEGORY, category);
            cv.put(COLUMN_DESCRIPTION, description);
            db.update(TABLE_SKILLS, cv, COLUMN_ID+" = ?", new String[] {cursor.getString(0)});
            result = true;
        }
        cursor.close();
        return result;
    }

    public boolean addProfile(Profile profile) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE, profile.getProfile());
        values.put(COLUMN_FNAME, profile.getFname());
        values.put(COLUMN_EMAIL, profile.getEmail());
        values.put(COLUMN_PHONE, profile.getPhone());
        values.put(COLUMN_BIRTHDAY, profile.getBirthday());
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.insert(TABLE_PROFILES, null, values);
        if(res==-1) {
            return false;
        }else{
            return true;
        }
    }

    public Profile findProfile(String profileName) {
        String query = "SELECT * FROM " + TABLE_PROFILES + " WHERE " + COLUMN_PROFILE+ " = '" + profileName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Profile profile = new Profile();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            profile.setId(Integer.parseInt(cursor.getString(0)));
            profile.setProfile(cursor.getString(1));
            profile.setFname(cursor.getString(2));
            profile.setEmail(cursor.getString(3));
            profile.setPhone(cursor.getString(4));
            profile.setBirthday(cursor.getString(5));
            cursor.close();
        } else {
            profile = null;
        }
        return profile;
    }


    public boolean deleteProfile(String profile) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_PROFILES + " WHERE " + COLUMN_PROFILE + " = '" + profile + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int deleted = db.delete(TABLE_PROFILES, COLUMN_PID + " = ?", new String[] { cursor.getString(0) });
            if(deleted>0){
                result = true;
            }
        }
        cursor.close();
        return result;
    }

    public boolean updateProfile(String oldProfile, String profile, String fname, String email,String phone,String birthday) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_PROFILES + " WHERE " + COLUMN_PROFILE + " = '" + oldProfile + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_PID, Integer.parseInt(cursor.getString(0)));
            cv.put(COLUMN_PROFILE, profile);
            cv.put(COLUMN_FNAME, fname);
            cv.put(COLUMN_EMAIL, email);
            cv.put(COLUMN_PHONE, phone);
            cv.put(COLUMN_BIRTHDAY, birthday);
            db.update(TABLE_PROFILES, cv, COLUMN_PID+" = ?", new String[] {cursor.getString(0)});
            result = true;
        }
        cursor.close();
        return result;
    }

    public Cursor getAllProfiles(){
        String query = "SELECT * FROM " + TABLE_PROFILES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
