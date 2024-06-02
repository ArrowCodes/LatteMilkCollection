package com.enlace.lattemilkcollection;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;

/**
 * Created by KELVIN on 26/05/2017.
 */
public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String TAG_TOKEN = "tagtoken";
    private static final String TAG_PROJECT_ID = "project_id";
    private static final String TAG_PROJECT_NAME = "project_name";
    private static final String TAG_ACTIVITY_INDEXER = "activity_indexer";
    private static final String TAG_ACTIVITY_NAME = "activity_name";
    private static final String TAG_COHORT_ID = "cohort_id";
    private static final String TAG_COHORT_NAME = "cohort_name";
    private static final String TAG_REF_NO = "ref_no";
    private RequestQueue requestQueue;


    private final static String SHARED_PREF_NAME = "mysharedpref12";
    private final static String KEY_USER_NAME = "username";
    private final static String KEY_USER_FNAME = "fname";
    private final static String KEY_USER_LNAME = "lname";
    private final static String KEY_SURNAME = "surname";


    /* private final static String KEY_USER_COUNTRY = "usercountry";*/

    private SharedPrefManager(Context context)
    {
        mCtx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    public boolean userLogin(String user_name, String fname, String lname, String surname)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME,user_name);
        editor.putString(KEY_USER_FNAME,fname);
        editor.putString(KEY_USER_LNAME,lname);
        editor.putString(KEY_SURNAME,surname);
        editor.apply();
        return true;
    }
    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USER_FNAME,null)!=null)
        {
            return true;
        }
        return false;
    }
    public boolean switchIsChecked()
    {
        SharedPreferences.Editor editor = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("checked", true);
        editor.commit();
        return true;
    }
    public boolean switchUnchecked()
    {
        SharedPreferences.Editor editor = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("checked", false);
        editor.commit();
        return true;
    }
    public boolean logout()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_FNAME,null);
        editor.apply();
        return true;
    }
    public String getKeyUserFname()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_FNAME,null);
    }
    public String getKeyUserLname()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_LNAME,null);
    }
    public String getKeyUserSurname()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SURNAME,null);
    }
    public String getKeyUserName()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, "");
        //return sharedPreferences.getString(String.valueOf(KEY_USER_ID),null);
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }
    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    //save project id
    public boolean saveProjectId(String project_id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_PROJECT_ID, project_id);
        editor.apply();
        return true;
    }
    //Get project id
    public String getTagProjectId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_PROJECT_ID, null);
    }

    //save project name
    public boolean saveProjectName(String project_name){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_PROJECT_NAME, project_name);
        editor.apply();
        return true;
    }
    //Get project id
    public String getTagProjectName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_PROJECT_NAME, null);
    }

    //save activity indexer
    public boolean saveActivityIndexer(String activity_indexer){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_ACTIVITY_INDEXER,activity_indexer);
        editor.apply();
        return true;
    }
    //Get Activity Indexer
    public String getActivityIndexer(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_ACTIVITY_INDEXER, null);
    }

    //save activity indexer
    public boolean saveActivityName(String activity_name){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_ACTIVITY_NAME,activity_name);
        editor.apply();
        return true;
    }
    //Get Activity Indexer
    public String getActivityName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_ACTIVITY_NAME,null);
    }

    //save cohort id
    public boolean saveCohortId(String cohort_id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_COHORT_ID,cohort_id);
        editor.apply();
        return true;
    }
    //Get Activity Indexer
    public String getTagCohortId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_COHORT_ID, null);
    }

    //save cohort id
    public boolean saveCohortName(String cohort_name){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_COHORT_NAME,cohort_name);
        editor.apply();
        return true;
    }
    //Get Activity Indexer
    public String getTagCohortName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_COHORT_NAME, null);
    }

    //save cohort id
    public boolean saveRefNo(String ref_no){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_REF_NO,ref_no);
        editor.apply();
        return true;
    }
    //Get Activity Indexer
    public String getRefNo(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_REF_NO, null);
    }
}