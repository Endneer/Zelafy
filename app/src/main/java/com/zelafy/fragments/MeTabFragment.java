package com.zelafy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zelafy.R;
import com.zelafy.utilities.FirebaseUtils;

public class MeTabFragment extends Fragment {


    TextView mEmailTextView;
    TextView mUserNameTextView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);

        mEmailTextView = (TextView) rootView.findViewById(R.id.tv_user_email);
        mUserNameTextView = (TextView) rootView.findViewById(R.id.tv_user_name);
        mDatabase = FirebaseUtils.getDatabase().getReference();
        mAuth = FirebaseAuth.getInstance();

        mEmailTextView.setText("Email : ");
        mUserNameTextView.setText("Name : ");

        //mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).keepSynced(true);
        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(String.class);
                mEmailTextView.append(email);

                String name = dataSnapshot.child("name").getValue(String.class);
                mUserNameTextView.append(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}

