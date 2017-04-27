package com.zelafy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zelafy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the view holder for viewing created conversations titles.
 * An Adapter for getting recycler view items from the backend.
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 * Created by endneer on 4/17/17.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    List<String> messages;
    List<String> messagesIds;
    private DatabaseReference mDatabase;

    public MessagesAdapter(final String receiverId, final String senderId) {
        messages = new ArrayList<>();
        messagesIds = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Messages").orderByChild("senderId")
                .equalTo(senderId).addChildEventListener(new ChildEventListener() {

            private boolean messageBelongsToThisReceiver(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("receiverId").getValue(String.class).equals(receiverId)) {
                    return true;
                }
                return false;
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (messageBelongsToThisReceiver(dataSnapshot)) {
                    String messageText = dataSnapshot.child("text").getValue(String.class);
                    messages.add(messageText);
                    messagesIds.add(dataSnapshot.getKey());
                    notifyItemInserted(messages.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (messageBelongsToThisReceiver(dataSnapshot)) {

                    String messageText = dataSnapshot.child("text").getValue(String.class);
                    String messageKey = dataSnapshot.getKey();

                    int messageIndex = messagesIds.indexOf(messageKey);
                    if (messageIndex > -1) {
                        messages.set(messageIndex, messageText);
                        notifyItemChanged(messageIndex);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (messageBelongsToThisReceiver(dataSnapshot)) {

                    String messageKey = dataSnapshot.getKey();

                    int messageIndex = messagesIds.indexOf(messageKey);
                    if (messageIndex > -1) {
                        messages.remove(messageIndex);
                        messagesIds.remove(messageIndex);
                        notifyItemRemoved(messageIndex);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.message_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MessagesViewHolder viewHolder = new MessagesViewHolder(view);


        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;

        public MessagesViewHolder(View itemView) {
            super(itemView);

            messageTextView = (TextView) itemView.findViewById(R.id.tv_message);
        }

        public void bind(String title) {
            messageTextView.setText(title);
        }
    }
}
