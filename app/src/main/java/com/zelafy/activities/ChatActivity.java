package com.zelafy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.zelafy.R;
import com.zelafy.utilities.User;
import com.zelafy.utilities.Message;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    MessagesList messagesList;
    String otherUserId;
    String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private MessagesListAdapter<Message> adapter;
    private ChildEventListener messagesEventListener;
    private String otherUserName;
    private MessageInput inputView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesList = (MessagesList) findViewById(R.id.messagesList);
        inputView = (MessageInput) findViewById(R.id.input);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        otherUserId = intent.getStringExtra("otherUserId");
        otherUserName = intent.getStringExtra("otherUserName");

        setTitle(otherUserName);

        currentUserId = mAuth.getCurrentUser().getUid();

        messagesEventListener = new ChildEventListener() {
            private boolean messageBelongsToThisReceiver(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("receiver_id").getValue(String.class).equals(otherUserId) && dataSnapshot.child("sender_id").getValue(String.class).equals(currentUserId))
                        || (dataSnapshot.child("sender_id").getValue(String.class).equals(otherUserId) && dataSnapshot.child("receiver_id").getValue(String.class).equals(currentUserId))) {
                    return true;
                }
                return false;
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (messageBelongsToThisReceiver(dataSnapshot)) {
                    String messageText = dataSnapshot.child("text").getValue(String.class);
                    User user = new User(dataSnapshot.child("sender_id").getValue(String.class), null, null, null);
                    Message message = new Message(dataSnapshot.getKey(), user, messageText);
                    adapter.addToStart(message, true);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.child("Messages").keepSynced(true);

        mDatabase.child("Messages").addChildEventListener(messagesEventListener);





        adapter = new MessagesListAdapter<>(currentUserId, null);
        messagesList.setAdapter(adapter);

        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {

                String key = mDatabase.child("Messages").push().getKey();

                SentMessage messageToBeSent = new SentMessage(input.toString(), currentUserId, otherUserId);

                Map<String, Object> postValues = messageToBeSent.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Messages/" + key, postValues);

                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeChildEventListener();
    }

    public void removeChildEventListener() {
        mDatabase.child("Messages").removeEventListener(messagesEventListener);
    }


    @IgnoreExtraProperties
    class SentMessage {
        String text;
        String senderId;
        String receiverId;

        public SentMessage() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }

        public SentMessage(String text, String senderId, String receiverId) {
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
