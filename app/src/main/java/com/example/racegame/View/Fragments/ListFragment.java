package com.example.racegame.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.racegame.Adapters.RecordAdapter;
import com.example.racegame.Interfaces.MapsCallback;
import com.example.racegame.Model.StudentRacer;
import com.example.racegame.R;

import java.util.ArrayList;


/**
 * ListFragment class hold list of record items
 */
public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MapsCallback mapsCallback;
    private ArrayList<StudentRacer> records;

    /**
     * onCreateView method
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        recyclerView = view.findViewById(R.id.record_LST_scores);
        initViews();
        return view;
    }


    /**
     * setCallbackMaps method
     * @param mapsCallback-MapsCallback
     */
    public void setCallbackMaps(MapsCallback mapsCallback) {
        this.mapsCallback = mapsCallback;
    }

    /**
     * initViews init all record-cards view by RecordAdapter
     */
    private void initViews() {
        if (records == null)
            return;
        RecordAdapter recordAdapter = new RecordAdapter(this.getContext(), records);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(recordAdapter);
        recordAdapter.setRecordCallback(mapsCallback);

    }

    /**
     * setRecords method
     * @param records-ArrayList<StudentRacer>
     */
    public void setRecords(ArrayList<StudentRacer> records) {
        this.records = records;
    }
}