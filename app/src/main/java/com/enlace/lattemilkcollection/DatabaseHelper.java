package com.enlace.lattemilkcollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "latte.db";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Farmers table
    private static final String TABLE_FARMERS = "farmers";
    private static final String COLUMN_FARMER_ID = "farmer_id";
    private static final String COLUMN_FARMER_NAME = "name";
    private static final String COLUMN_FARMER_LAT = "lat";
    private static final String COLUMN_FARMER_LNG = "lng";
    private static final String COLUMN_RATE_PER_LITRE = "rate_per_litre";
    private static final String COLUMN_PAYMENT_MODE = "payment_mode";
    private static final String COLUMN_ACCOUNT = "account";
    private static final String COLUMN_PAY_DAY = "pay_day";
    private static final String COLUMN_PNUMBER = "pnumber";
    private static final String COLUMN_ROUTE = "route";
    private static final String COLUMN_CLOUD_SYNC_KEY = "sync_key";
    private static final String COLUMN_USERNAME = "username";

    // Routes table
    private static final String TABLE_ROUTES = "routes";
    private static final String COLUMN_ROUTE_ID = "route_id";
    private static final String COLUMN_ROUTE_NAME = "route_name";
    private static final String COLUMN_ROUTE_SYNC_KEY = "sync_key";
    private static final String COLUMN_ROUTE_USERNAME = "user_name";

    // Sales table
    private static final String TABLE_SALES = "sales";
    private static final String COLUMN_SALES_ID = "sales_id";
    private static final String COLUMN_SALES_FARMER_ID = "farmer_id";
    private static final String COLUMN_SALES_RATE_PER_LITRE = "rate_per_litre";
    private static final String COLUMN_SALES_LITRES = "litres";
    private static final String COLUMN_SALES_AMOUNT_TO_PAY = "amount_to_pay";
    private static final String COLUMN_SALES_SYNC_KEY = "sync_key";
    private static final String COLUMN_SYS_DATE = "sys_date";
    private static final String COLUMN_SYS_TIME = "sys_time";


    // Users Client table
    private static final String TABLE_USERS_CLIENT = "users_client";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_CODE  = "user_code";
    private static final String COLUMN_CLIENT_ID = "client_id";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_FIRST_NAME = "firstname";
    private static final String COLUMN_MIDDLE_NAME = "middlename";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_EMAIL = "email";

    // SQL to create farmers table
    private static final String CREATE_TABLE_FARMERS = "CREATE TABLE " + TABLE_FARMERS + " (" +
            COLUMN_FARMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FARMER_NAME + " TEXT, " +
            COLUMN_FARMER_LAT + " REAL, " +
            COLUMN_FARMER_LNG + " REAL, " +
            COLUMN_RATE_PER_LITRE + " REAL, " +
            COLUMN_PAYMENT_MODE + " TEXT, " +
            COLUMN_ACCOUNT + " TEXT, " +
            COLUMN_PAY_DAY + " TEXT, " +
            COLUMN_PNUMBER + " TEXT, " +
            COLUMN_ROUTE + " TEXT, " +
            COLUMN_CLOUD_SYNC_KEY + " TEXT UNIQUE, " +
            COLUMN_USERNAME + " TEXT" + ")";

    // SQL to create routes table
    private static final String CREATE_TABLE_ROUTES = "CREATE TABLE " + TABLE_ROUTES + " (" +
            COLUMN_ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ROUTE_NAME + " TEXT UNIQUE, " +
            COLUMN_ROUTE_SYNC_KEY + " TEXT UNIQUE, " +
            COLUMN_ROUTE_USERNAME + " TEXT" + ")";

    // SQL to create sales table
    private static final String CREATE_TABLE_SALES = "CREATE TABLE " + TABLE_SALES + " (" +
            COLUMN_SALES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SALES_FARMER_ID + " INTEGER, " +
            COLUMN_SALES_RATE_PER_LITRE + " REAL, " +
            COLUMN_SALES_LITRES + " REAL, " +
            COLUMN_SALES_AMOUNT_TO_PAY + " REAL, " +
            COLUMN_SALES_SYNC_KEY + " TEXT UNIQUE, " +
            COLUMN_SYS_DATE + " TEXT, " +
            COLUMN_SYS_TIME + " TEXT, " +
            "FOREIGN KEY (" + COLUMN_SALES_FARMER_ID + ") REFERENCES " + TABLE_FARMERS + "(" + COLUMN_FARMER_ID + ")" + ")";

    // SQL to create routes table
    private static final String CREATE_TABLE_USERS_CLIENT = "CREATE TABLE " + TABLE_USERS_CLIENT + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_NAME + " TEXT UNIQUE, " +
            COLUMN_USER_CODE + " TEXT, " +
            COLUMN_CLIENT_ID + " TEXT, " +
            COLUMN_LEVEL + " TEXT, " +
            COLUMN_FIRST_NAME + " TEXT, " +
            COLUMN_MIDDLE_NAME + " TEXT, " +
            COLUMN_SURNAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT" + ")";


    public long insertUsersClient(String user_name, String user_code,String client_id,String level,String firstname,String middlename,String surname,String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", user_name);
        values.put("user_code", user_code);
        values.put("client_id", client_id);
        values.put("level", level);
        values.put("firstname", firstname);
        values.put("middlename", middlename);
        values.put("surname", surname);
        values.put("email", email);
        return db.insertWithOnConflict("users_client", null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }


    public long insertFarmers(String farmer_name, String farmer_lat,String farmer_lng,String rate_per_litre,String payment_mode,String account,String pay_day,String pnumber,String route,String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("farmer_name", farmer_name);
        values.put("farmer_lat", farmer_lat);
        values.put("farmer_lng", farmer_lng);
        values.put("farmer_lng", farmer_lng);
        values.put("rate_per_litre", rate_per_litre);
        values.put("payment_mode", payment_mode);
        values.put("account", account);
        values.put("pay_day",pay_day);
        values.put("pnumber",pnumber);
        values.put("route",route);
        values.put("user_name",user_name);
        return db.insertWithOnConflict("farmers", null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public long insertRoutes(String route_name, String sync_key,String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("route_name", route_name);
        values.put("sync_key", sync_key);
        values.put("user_name", user_name);
        return db.insertWithOnConflict("routes", null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public long insertSales(String farmer_id, String rate_per_litre,final String litres,String amount_to_pay,String sys_date,String sys_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("farmer_id", farmer_id);
        values.put("rate_per_litre", rate_per_litre);
        values.put("litres", litres);
        values.put("amount_to_pay", amount_to_pay);
        values.put("sys_date", sys_date);
        values.put("sys_time", sys_time);
        return db.insertWithOnConflict("sales", null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean loginUser(String user_name, String user_code, Context context, Class<?> targetActivity) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"user_name", "firstname", "middlename", "surname"};
        String selection = "user_name=? AND user_code=?";
        String[] selectionArgs = {user_name, user_code};

        Cursor cursor = db.query("users_client", columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String fetchedUserName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
            String fetchedFirstname = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            String fetchedMiddlename = cursor.getString(cursor.getColumnIndexOrThrow("middlename"));
            String fetchedSurname = cursor.getString(cursor.getColumnIndexOrThrow("surname"));

            // Store details in SharedPreferences
            SharedPrefManager.getInstance(context).userLogin(fetchedUserName, fetchedFirstname, fetchedMiddlename, fetchedSurname);
            cursor.close();

            // Start the next activity
           /* Intent intent = new Intent(context, targetActivity);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);*/

            return true;  // Login successful
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return false;  // Login failed
        }
    }


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FARMERS);
        db.execSQL(CREATE_TABLE_ROUTES);
        db.execSQL(CREATE_TABLE_SALES);
        db.execSQL(CREATE_TABLE_USERS_CLIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS_CLIENT);
        onCreate(db);
    }
}
