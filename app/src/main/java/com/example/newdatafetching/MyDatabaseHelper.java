package com.example.newdatafetching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EmployeeInfo.db";
    private static final String ID = "_id";
    private static final String TABLE_NAME = "Employee_info";
    private static final int DATABASE_VERSION_NO = 1;
    private static final String NAME = "Name";
    private static final String latitude = "Latitude";
    private static final String longitude = "Longitude";
    private static final String fetch = "Fetch";
    private static final String SELECT_NAME = "SELECT * FROM "+TABLE_NAME+" WHERE Name = ";
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255),"
            +latitude+" REAL, "+longitude+" REAL,"+fetch+" INTEGER);";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String SELECT_ALL = "SELECT * FROM "+TABLE_NAME;



    /*private static final String DATABASE_NAME = "student.db";
    private static final String TABLE_NAME = "student_details";
    private static final String ID = "_id";
    private static final String NAME = "Name";
    private static final String AGE = "Age";
    private static final int DATABASE_VERSION_NO = 1;
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME+" VARCHAR(255),"+AGE+" INTEGER);";
    */
    private Context context;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION_NO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context,"Table is created",Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(context,"Exception: "+e,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
            Toast.makeText(context,"Changed!",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(context,"Exception: "+e,Toast.LENGTH_LONG).show();
        }

    }

    public Cursor checkData(String checkName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_NAME+"'"+checkName+"'"+" ;",null);
        return cursor;
    }

    public long insertData(String names, double latitudes, double longitudes){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor resultSet = checkData(names);
        if (resultSet.getCount()==0){
            contentValues.put(NAME,names);
            contentValues.put(longitude,longitudes);
            contentValues.put(latitude,latitudes);
            contentValues.put(fetch,1);
            long rowId = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
            return rowId;
        }
        else {
            //Toast.makeText(context,"Data is already here!",Toast.LENGTH_SHORT).show();
            return -1;
        }
    }
    public Cursor retrieveData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL,null);
        return cursor;
    }

    public void updateData(String name, Double lat, Double longt){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(latitude,lat);
        contentValues.put(longitude,longt);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"Name = ?",new String[]{name});
        //return true;
    }
}
