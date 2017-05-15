package com.zelafy.utilities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by endneer on 5/15/17.
 */

public class User implements IUser {
    String id;
    String name;
    String avatar;
    String email;



    public User(String id, String email, String name, String avatar) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
    }

    /**
     * Returns the user's id
     *
     * @return the user's id
     */
    @Override
    public String getId() {
        return id;
    }

    public String getEmail() { return email; }

    /**
     * Returns the user's name
     *
     * @return the user's name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the user's avatar image url
     *
     * @return the user's avatar image url
     */
    @Override
    public String getAvatar() {
        return avatar;
    }
}
