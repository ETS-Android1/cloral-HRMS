package com.noideastudios.hrms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "candidates.db";
    private static final String TABLE_CANDIDATES = "candidates";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_STATUS = "status";

    public DBhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_CANDIDATES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " VARCHAR(30), " + COLUMN_PHONE + " NUMBER(10), " + COLUMN_POSITION + " VARCHAR(30), " + COLUMN_STATUS + " VARCHAR(15));";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CANDIDATES);
        onCreate(sqLiteDatabase);
    }

    public void addCandidate(Candidate candidate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, candidate.getName());
        contentValues.put(COLUMN_PHONE, candidate.getPhone());
        contentValues.put(COLUMN_POSITION, candidate.getPosition());
        contentValues.put(COLUMN_STATUS, candidate.getStatus());
        sqLiteDatabase.insert(TABLE_CANDIDATES, null, contentValues);
        sqLiteDatabase.close();
    }

    public StringBuffer databaseToString() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CANDIDATES + " WHERE " + COLUMN_STATUS + " = 'Selected'";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        StringBuffer ex = new StringBuffer();
        while (!cursor.isAfterLast()) {
            ex.append(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)).toUpperCase());
            ex.append("\nPhone: ").append(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
            ex.append("\nPosition: ").append(cursor.getString(cursor.getColumnIndex(COLUMN_POSITION)));
            ex.append("\nStatus: ").append(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
            cursor.moveToNext();
            ex.append("\n\n");
        }
        cursor.close();
        sqLiteDatabase.close();
        return ex;
    }

    public Candidate returnSelectedCandidate() {
        Candidate candidate = new Candidate();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CANDIDATES + " WHERE " + COLUMN_STATUS + " = 'Selected'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                candidate.setId(cursor.getColumnIndex(COLUMN_ID));
                candidate.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                candidate.setPhone(cursor.getColumnIndex(COLUMN_PHONE));
                candidate.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return candidate;
    }

    public void deleteAll() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_CANDIDATES);
    }
}
