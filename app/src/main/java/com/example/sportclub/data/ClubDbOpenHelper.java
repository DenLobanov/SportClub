package com.example.sportclub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ClubDbOpenHelper extends SQLiteOpenHelper {
    public ClubDbOpenHelper(@Nullable Context context) {
        super(context, ClubContract.DATABASE_NAME, null, ClubContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MEMBERS_TABLE = "CREATE TABLE " + ClubContract.MemberEntry.TABLE_NAME + "(" + ClubContract.MemberEntry.KEY_ID + " INTEGER PRIMARY KEY,"
                + ClubContract.MemberEntry.KEY_FIRST_NAME + " TEXT,"
                + ClubContract.MemberEntry.KEY_LAST_NAME + " TEXT,"
                + ClubContract.MemberEntry.KEY_GENDER + " INTEGER NOT NULL,"
                + ClubContract.MemberEntry.KEY_KIND + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_MEMBERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClubContract.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}
