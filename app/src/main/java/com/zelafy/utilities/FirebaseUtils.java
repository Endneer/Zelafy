package com.zelafy.utilities;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by endneer on 4/27/17.
 */

public class FirebaseUtils {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
