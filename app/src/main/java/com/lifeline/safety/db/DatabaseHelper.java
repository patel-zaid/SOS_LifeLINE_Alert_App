package com.lifeline.safety.db;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import androidx.annotation.Nullable;

import com.lifeline.safety.models.AlertHistory;
import com.lifeline.safety.models.Contact;
import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lifeline.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CONTACTS = "EmergencyContacts";
    public static final String CONTACT_ID = "id";
    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_PHONE = "phone";

    public static final String TABLE_HISTORY = "AlertHistory";
    public static final String HISTORY_ID = "id";
    public static final String HISTORY_DATE = "date";
    public static final String HISTORY_TIME = "time";
    public static final String HISTORY_LOCATION = "location";

    public DatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createContactsTable = "CREATE TABLE " + TABLE_CONTACTS + " (" + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CONTACT_NAME + " TEXT NOT NULL, " + CONTACT_PHONE + " TEXT NOT NULL);";

        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + " (" + HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HISTORY_DATE + " TEXT, " + HISTORY_TIME + " TEXT, " + HISTORY_LOCATION + " TEXT);";

        db.execSQL(createContactsTable);
        db.execSQL(createHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public boolean insertContact(String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, name);
        values.put(CONTACT_PHONE, phone);

        long result = db.insert(TABLE_CONTACTS, null, values);
        return result != -1;
    }

    public List<Contact> getAllContacts() {
        List<Contact> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + CONTACT_ID + " DESC", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new Contact(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
            }
            cursor.close();
        }
        return list;
    }


    public void deleteContact(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS, CONTACT_ID + "=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<String> getAllContactPhones(){
        ArrayList<String> phones = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + CONTACT_PHONE + " FROM " + TABLE_CONTACTS, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                phones.add(cursor.getString(0));
            }
            cursor.close();
        }
        return phones;
    }

    public void insertAlertHistory(String date, String time, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HISTORY_DATE, date);
        values.put(HISTORY_TIME, time);
        values.put(HISTORY_LOCATION, location);
        db.insert(TABLE_HISTORY, null, values);
    }

    public ArrayList<AlertHistory> getAllAlertHistory(){
        ArrayList<AlertHistory> history = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORY, null, null, null, null, null, HISTORY_ID + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                history.add(new AlertHistory(
                        cursor.getInt(cursor.getColumnIndexOrThrow(HISTORY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(HISTORY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(HISTORY_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(HISTORY_LOCATION))
                ));
            }
            cursor.close();
        }
        return history;
    }

    public void clearAlertHistory() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HISTORY, null, null);
    }
}