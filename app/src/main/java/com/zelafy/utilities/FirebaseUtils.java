package com.zelafy.utilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by endneer on 4/27/17.
 */

public class FirebaseUtils {
    private static FirebaseDatabase mDatabase;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static FirebaseAuth getAuth() {
        return mAuth;
    }
}
