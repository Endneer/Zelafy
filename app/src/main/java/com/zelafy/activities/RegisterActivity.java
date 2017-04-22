package com.zelafy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zelafy.R;

public class RegisterActivity extends AppCompatActivity {

    //UI Refrences
    private EditText mEmailView;
    private EditText mNameView;
    private EditText mPasswordView;
    private Button mButtonView;

    //Authentication Refrences
    private FirebaseAuth mAuth;

    //toast for signing in
    private ProgressDialog mProgress;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView =(EditText) findViewById(R.id.RegisterEmail);
        mNameView = (EditText) findViewById(R.id.RegisterName);
        mPasswordView = (EditText) findViewById(R.id.RegisterPassword);
        mButtonView = (Button) findViewById(R.id.RegisterButton);

        mAuth = FirebaseAuth.getInstance();

        //Refere to the database and create a new child instead of storing the data directly in firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUp(v);
            }
        });
    }
    public void SignUp(View view){
        //Getting User infotmation

        final String getEmail = mEmailView.getText().toString();
        final String getName = mNameView.getText().toString();
        final String getPassword = mPasswordView.getText().toString();

        if (!TextUtils.isEmpty(getEmail) && !TextUtils.isEmpty(getName) && !TextUtils.isEmpty(getPassword)){


            mProgress.setMessage("Signing Up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(getEmail,getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        //get the UID of the user
                        String userId = mAuth.getCurrentUser().getUid();

                        //create a new child and store it in a refrence to the database
                        DatabaseReference currentUserDatabase = mDatabase.child(userId);

                        //storing in database
                        currentUserDatabase.child("name").setValue(getName);
                        currentUserDatabase.child("email").setValue(getEmail);
                        currentUserDatabase.child("profile picture").setValue("default");
                        currentUserDatabase.child("password").setValue(getPassword);


                        //stop the progressing dialog
                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);

                    }
                }
            });
        }



    }
}
