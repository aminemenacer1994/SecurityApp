package com.example.amine.securityapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by n0582158 on 17/04/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    //database
    static String DATABASE_NAME="UserDataBase";
    public static final String TABLE_NAME="UserTable";
    public static final String Table_Column_ID="id";
    public static final String Table_Column_1_Name="name";
    public static final String Table_Column_2_Email="email";
    public static final String Table_Column_3_Password="password";


    public SQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY, "+Table_Column_1_Name+" VARCHAR, "+Table_Column_2_Email+" VARCHAR, "+Table_Column_3_Password+" VARCHAR, "+
                                            " VARCHAR)";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(db);

    }


    //add data to database
    public boolean insertData(String name, String email, String password, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table_Column_ID, id);
        contentValues.put(Table_Column_1_Name, name);
        contentValues.put(Table_Column_2_Email, email);
        contentValues.put(Table_Column_3_Password, password);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;

        }else {
            return true;
        }

    }

    //display data stored in the database
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }



    //update data in database
    public boolean updateData(String name,String email,String password, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Table_Column_ID, id);
        contentValues.put(Table_Column_1_Name, name);
        contentValues.put(Table_Column_2_Email, email);
        contentValues.put(Table_Column_3_Password, password);


        db.update(TABLE_NAME, contentValues, "id  = ?", new  String[] {id});
        return true;
    }

    //delete data from database
    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[] {id});
    }
}
