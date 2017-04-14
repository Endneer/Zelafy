package com.zelafy;

import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;

public class Chat  extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chats, container, false);

        return rootView;
    }
}

