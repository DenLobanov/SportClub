package com.example.sportclub.data;

import android.net.Uri;

public final class ClubContract {

    private ClubContract() {

    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Club";
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.sportclub";
    public static final String PATH_MEMBERS = "members";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);
    public static final class MemberEntry {

        public static final String TABLE_NAME = "members";
        public static final String KEY_ID = "_id";
        public static final String KEY_FIRST_NAME = "firstName";
        public static final String KEY_LAST_NAME = "lastName";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_KIND = "kind";
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);

        public static final String CONTENT_MULTIPLE_ITEMS = "vnd.android.cursor.dir/" + AUTHORITY + PATH_MEMBERS;
        public static final String CONTENT_SINGLE_ITEM = "vnd.android.cursor.item" + AUTHORITY + PATH_MEMBERS;
    }
}
