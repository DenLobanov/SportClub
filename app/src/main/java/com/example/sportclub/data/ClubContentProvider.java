package com.example.sportclub.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URI;

public class ClubContentProvider extends ContentProvider {

    ClubDbOpenHelper dbOpenHelper;

    private static final int MEMBERS = 1;
    private static final int MEMBERS_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(ClubContract.AUTHORITY, ClubContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(ClubContract.AUTHORITY, ClubContract.PATH_MEMBERS + "/#", MEMBERS_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new ClubDbOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                cursor = db.query(ClubContract.MemberEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            case MEMBERS_ID:
                s = ClubContract.MemberEntry.KEY_ID + "=?";
                strings1 = new String[] {String .valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ClubContract.MemberEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            default:
                Toast.makeText(getContext(), "Incorrect uri", Toast.LENGTH_SHORT).show();
                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                return ClubContract.MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case MEMBERS_ID:
                return ClubContract.MemberEntry.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String firstName = contentValues.getAsString(ClubContract.MemberEntry.KEY_FIRST_NAME);
        if (firstName == null) {
            throw new IllegalArgumentException("You have to input first name");
        }
        String lastName = contentValues.getAsString(ClubContract.MemberEntry.KEY_LAST_NAME);
        if (lastName == null) {
            throw new IllegalArgumentException("You have to input last name");
        }
        Integer gender = contentValues.getAsInteger(ClubContract.MemberEntry.KEY_GENDER);
        if (gender == null || !(gender == ClubContract.MemberEntry.GENDER_UNKNOWN || gender == ClubContract.MemberEntry.GENDER_FEMALE || gender == ClubContract.MemberEntry.GENDER_MALE)) {
            throw new IllegalArgumentException("You have to input correct gender");
        }
        String sport = contentValues.getAsString(ClubContract.MemberEntry.KEY_KIND);
        if (sport == null) {
            throw new IllegalArgumentException("You have to input kind");
        }
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                long id = db.insert(ClubContract.MemberEntry.TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.e("Insert: ", "Insertion of data in the table failed");
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                return null;
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MEMBERS:
                rowsDeleted = db.delete(ClubContract.MemberEntry.TABLE_NAME, s, strings);
                break;
            case MEMBERS_ID:
                s = ClubContract.MemberEntry.KEY_ID + "=?";
                strings = new String[] {String .valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ClubContract.MemberEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Can't delete this URI");
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_FIRST_NAME)) {
            String firstName = contentValues.getAsString(ClubContract.MemberEntry.KEY_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name");
            }
        }
        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_LAST_NAME)) {
            String lastName = contentValues.getAsString(ClubContract.MemberEntry.KEY_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last name");
            }
        }
        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_GENDER)) {
            Integer gender = contentValues.getAsInteger(ClubContract.MemberEntry.KEY_GENDER);
            if (gender == null || !(gender == ClubContract.MemberEntry.GENDER_UNKNOWN || gender == ClubContract.MemberEntry.GENDER_FEMALE || gender == ClubContract.MemberEntry.GENDER_MALE)) {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        }
        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_KIND)) {
            String sport = contentValues.getAsString(ClubContract.MemberEntry.KEY_KIND);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input kind");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MEMBERS:
                rowsUpdated =  db.update(ClubContract.MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case MEMBERS_ID:
                s = ClubContract.MemberEntry.KEY_ID + "=?";
                strings = new String[] {String .valueOf(ContentUris.parseId(uri))};
                rowsUpdated =  db.update(ClubContract.MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Can't update this URI");
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
