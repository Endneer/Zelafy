package com.zelafy.activities;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.zelafy.R;
import com.zelafy.adapters.MessagesAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    MessagesAdapter mAdapter;
    RecyclerView mMessagesList;
    String receiverId;
    String senderId;
    Button mSendButton;
    EditText mMessageEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessagesList = (RecyclerView) findViewById(R.id.rv_messages);
        mSendButton = (Button) findViewById(R.id.btn_send);
        mMessageEditText = (EditText) findViewById(R.id.et_message_to_be_sent);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMessagesList.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        receiverId = intent.getStringExtra(Intent.EXTRA_TEXT);

        senderId = mAuth.getCurrentUser().getUid();

        mAdapter = new MessagesAdapter(receiverId, senderId);
        mMessagesList.setAdapter(mAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToBeSent = mMessageEditText.getText().toString();

                String key = mDatabase.child("Messages").push().getKey();

                Message messageToBeSent = new Message(textToBeSent, senderId, receiverId);

                Map<String, Object> postValues = messageToBeSent.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Messages/" + key, postValues);

                mDatabase.updateChildren(childUpdates);

                mMessageEditText.setText("");
            }
        });

    }

    @IgnoreExtraProperties
    class Message {
        String text;
        String senderId;
        String receiverId;

        public Message() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }

        public Message(String text, String senderId, String receiverId) {
            this.text = text;
            this.senderId = senderId;
            this.receiverId = receiverId;
        }

        public Map toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("text", text);
            result.put("sender_id", senderId);
            result.put("receiver_id", receiverId);

            return result;
        }

    }

}
