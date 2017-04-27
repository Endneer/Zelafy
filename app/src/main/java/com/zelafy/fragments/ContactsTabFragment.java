package com.zelafy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zelafy.R;
import com.zelafy.activities.ChatActivity;
import com.zelafy.adapters.ContactsAdapter;


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
    public void onContactClick(int clickedContactIndex) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        String receiverId = mAdapter.getContactsIds().get(clickedContactIndex);
        intent.putExtra(Intent.EXTRA_TEXT, receiverId);
        startActivity(intent);
    }
}

