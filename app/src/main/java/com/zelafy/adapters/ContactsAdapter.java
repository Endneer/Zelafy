package com.zelafy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zelafy.R;

import java.util.ArrayList;

/**
 * This class contains the view holder for viewing added contacts names.
 * An Adapter for getting recycler view items from the backend.
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 * Created by endneer on 4/20/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    String[] contactsNamesAsStrings = {"Software group", "OS group", "John", "Doe", "Smith", "Friends"};

    final private ContactClickListener mOnClickListener;

    public interface ContactClickListener {
        void onContactClick(int clickedContactIndex);
    }

    public ContactsAdapter(ContactClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    /**
     *
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

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
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
        holder.bind(contactsNamesAsStrings[position]);
    }

    @Override
    public int getItemCount() {
        return contactsNamesAsStrings.length;
    }

    public void setContactsNamesAsStrings(String[] contactsNamesAsStrings) {
        this.contactsNamesAsStrings = contactsNamesAsStrings;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contactName;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            contactName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            itemView.setOnClickListener(this);
        }

        public void bind(String title) {
            contactName.setText(title);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onContactClick(clickedPosition);
        }
    }
}
