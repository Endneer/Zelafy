package com.zelafy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zelafy.R;
import com.zelafy.utilities.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the view holder for viewing added contacts names.
 * An Adapter for getting recycler view items from the backend.
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 * Created by endneer on 4/20/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    //String[] contactsNamesAsStrings = {"Software group", "OS group", "John", "Doe", "Smith", "Friends"};

    List<String> contactsNames;
    List<String> contactsIds;
    List<String> contactsEmails;
    private DatabaseReference mDatabase;
    private ChildEventListener contactsEventListener;


    final private ContactClickListener mOnClickListener;

    public interface ContactClickListener {
        void onContactClick(int clickedContactIndex);
    }


    public ContactsAdapter(ContactClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        contactsNames = new ArrayList<>();
        contactsIds = new ArrayList<>();
        contactsEmails = new ArrayList<>();
        mDatabase = FirebaseUtils.getDatabase().getReference();
        contactsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                contactsNames.add(name);
                contactsEmails.add(email);
                contactsIds.add(dataSnapshot.getKey());
                notifyItemInserted(contactsNames.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String nameKey = dataSnapshot.getKey();
                String email = dataSnapshot.child("email").getValue(String.class);

                int nameIndex = contactsIds.indexOf(nameKey);
                if (nameIndex > -1) {
                    contactsNames.set(nameIndex, name);
                    contactsEmails.set(nameIndex, email);
                    notifyItemChanged(nameIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String nameKey = dataSnapshot.getKey();

                int nameIndex = contactsIds.indexOf(nameKey);
                if (nameIndex > -1) {
                    contactsNames.remove(nameIndex);
                    contactsIds.remove(nameIndex);
                    contactsEmails.remove(nameIndex);
                    notifyItemRemoved(nameIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        mDatabase.child("Users").addChildEventListener(contactsEventListener);
        mDatabase.child("Users").keepSynced(true);
    }

    public void removeChildEventListener() {
        mDatabase.child("Users").removeEventListener(contactsEventListener);
    }

    public List<String> getContactsNames() {
        return contactsNames;
    }

    public List<String> getContactsIds() {
        return contactsIds;
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
    public ContactsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.contacts_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ContactsViewHolder viewHolder = new ContactsViewHolder(view);

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
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        holder.bind(contactsNames.get(position),contactsEmails.get(position));
    }

    @Override
    public int getItemCount() {
        return contactsNames.size();
    }


    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contactName;
        String contactNameFirstChar;
        TextView firstChar;
        TextView contactEmail;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            contactName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            firstChar = (TextView) itemView.findViewById(R.id.tv_circle);
            contactEmail = (TextView) itemView.findViewById(R.id.tv_email);
            itemView.setOnClickListener(this);
        }

        public void bind(String name, String email) {

            contactName.setText(name);
            contactNameFirstChar = name.substring(0,1);
            firstChar.setText(contactNameFirstChar);
            contactEmail.setText(email);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onContactClick(clickedPosition);
        }
    }
}
