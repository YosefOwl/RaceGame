package com.example.racegame.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.racegame.Interfaces.CallbackMaps;
import com.example.racegame.R;


public class ListFragment extends Fragment {



    private CallbackMaps callbackMaps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        return view;
    }

    public ListFragment setCallbackMaps(CallbackMaps callbackMaps) {
        this.callbackMaps = callbackMaps;
        return this;
    }
}