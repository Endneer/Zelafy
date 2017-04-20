package com.zelafy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zelafy.R;

/**
 * This class contains the view holder for viewing created conversations titles.
 * An Adapter for getting recycler view items from the backend.
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 * Created by endneer on 4/17/17.
 */

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {

    String[] chatsTitlesAsStrings = {"Software group", "OS group", "John", "Doe", "Smith", "Friends"};


    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public ChatsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.chat_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        ChatsViewHolder viewHolder = new ChatsViewHolder(view);

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
    public void onBindViewHolder(ChatsViewHolder holder, int position) {
        holder.bind(chatsTitlesAsStrings[position]);
    }

    @Override
    public int getItemCount() {
        return chatsTitlesAsStrings.length;
    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder {

        TextView chatTitle;

        public ChatsViewHolder(View itemView) {
            super(itemView);

            chatTitle = (TextView) itemView.findViewById(R.id.tv_chat_title);
        }

        public void bind(String title) {
            chatTitle.setText(title);
        }
    }
}
