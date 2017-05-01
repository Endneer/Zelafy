package com.zelafy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.zelafy.R;
import com.zelafy.activities.ChatActivity;
import com.zelafy.adapters.ContactsAdapter;
import com.zelafy.utilities.FirebaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactsTabFragment extends Fragment implements ContactsAdapter.ContactClickListener {

    /**
     * References to RecyclerView and Adapter
     */
    private ContactsAdapter mAdapter;
    private RecyclerView mContactsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);


        /**
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mContactsList = (RecyclerView) rootView.findViewById(R.id.rv_contacts);

        /**
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list depending on which parameter you pass in to the LinearLayoutManager
         * constructor. By default, if you don't specify an orientation, you get a vertical list.
         * In our case, we want a vertical list, so we don't need to pass in an orientation flag to
         * the LinearLayoutManager constructor.
         *
         * There are other LayoutManagers available to display your data in uniform grids,
         * staggered grids, and more! See the developer documentation for more details.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mContactsList.setLayoutManager(layoutManager);

        /**
         * The ContactsAdapter is responsible for displaying each item in the list.
         */
        mAdapter = new ContactsAdapter(this);
        mContactsList.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.removeChildEventListener();
    }

    @Override
    public void onContactClick(String clickedContactId) {

        // TODO: 30/04/17 Check if there is existing chat between these two users 

        String currentUserId = FirebaseUtils.getAuth().getCurrentUser().getUid();
        Chat mChat = new Chat();  //send Chat name
        mChat.addMember(currentUserId);
        mChat.addMember(clickedContactId);

        String key = FirebaseUtils.getDatabase().getReference().child("Chats").push().getKey();

        Map<String, Object> postValues = mChat.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Chats/" + key + "/members", postValues);
        FirebaseUtils.getDatabase().getReference().updateChildren(childUpdates);

        //Set Chat Name
        FirebaseUtils.getDatabase().getReference().child("Messages").child(key).child("Name").setValue(mChat.getChatName());


        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, clickedContactId);
        startActivity(intent);
    }

    @IgnoreExtraProperties
    class Chat {
        List<String> membersIds = new ArrayList<>();
        String chatName;

        public Chat() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
            chatName = null;
        }

        public Chat(String chatName){
            this.chatName = chatName;
        }

        public String getChatName() {
            return chatName;
        }

        public void addMember(String member) {
            membersIds.add(member);
        }

        public Map toMap() {
            HashMap<String, Object> result = new HashMap<>();
            for (int i = 0; i < membersIds.size(); i++) {
                result.put("member"+(i+1), membersIds.get(i));
            }


            return result;
        }

    }

}

