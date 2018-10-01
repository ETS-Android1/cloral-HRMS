package com.noideastudios.hrms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "candidates.db";
    private static final String TABLE_CANDIDATES = "candidatetable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_PHOTO = "photo";

    DBhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_CANDIDATES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " VARCHAR(30), " + COLUMN_PHONE + " VARCHAR(10), " + COLUMN_POSITION + " VARCHAR(30), " + COLUMN_STATUS + " VARCHAR(15), " + COLUMN_PHOTO + " VARCHAR(50));";
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
        contentValues.put(COLUMN_PHOTO, candidate.getPhotoURI());
        sqLiteDatabase.insert(TABLE_CANDIDATES, null, contentValues);
        sqLiteDatabase.close();
    }

    public ArrayList<Candidate> returnCandidates(int number) {

        String query = "";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (number == 0)
            query = "SELECT * FROM " + TABLE_CANDIDATES + ";";
        else if (number == 1)
            query = "SELECT * FROM " + TABLE_CANDIDATES + " WHERE " + COLUMN_STATUS + " = 'Selected'";

        ArrayList<Candidate> candidateArrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Candidate candidate = new Candidate();
                candidate.setId(cursor.getColumnIndex(COLUMN_ID));
                candidate.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                candidate.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                candidate.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                candidate.setPhotoURI(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO)));
                candidateArrayList.add(candidate);
                cursor.moveToNext();
            }
            cursor.close();
        }

        sqLiteDatabase.close();
        return candidateArrayList;
    }

    public void deleteAll() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CANDIDATES, null, null);
    }
}
