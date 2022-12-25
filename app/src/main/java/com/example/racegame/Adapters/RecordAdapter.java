package com.example.racegame.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.racegame.Interfaces.MapsCallback;
import com.example.racegame.Model.StudentRacer;
import com.example.racegame.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

/**
 * Adapter to RecyclerView include inner class
 * to hold card views
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private ArrayList<StudentRacer> records;
    private MapsCallback mapsCallback;

    /**
     * RecordAdapter contractor
     * @param context-Context
     * @param records-ArrayList<StudentRacer>
     */
    public RecordAdapter(Context context, ArrayList<StudentRacer> records) {
        this.context = context;
        this.records = records;
    }

    /**
     * setRecordCallback method
     * @param mapsCallback-MapsCallback
     */
    public void setRecordCallback(MapsCallback mapsCallback) {
        this.mapsCallback = mapsCallback;
    }

    /**
     * onCreateViewHolder  method inflate view
     * @param parent-ViewGroup
     * @param viewType-int
     * @return view-View
     */
    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);

        return new RecordViewHolder(view);
    }

    /**
     * onBindViewHolder method bind items in recyclingView
     * @param holder-RecordAdapter.RecordViewHolder
     * @param position-int
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordViewHolder holder, int position) {

        StudentRacer record = getItem(position);

        holder.score_LBL_name.setText("Name : " + record.getName());
        holder.score_LBL_record.setText("Scores : " + record.getScore() + "");
        holder.score_IMG_location_icon.setImageResource(R.drawable.placeholder);

    }

    /**
     * getItemCount method return the size of records list
     * @return count-int
     */
    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }

    /**
     * getItem method return StudentRacer
     * @param position-int
     * @return StudentRacer
     */
    private StudentRacer getItem(int position) {
        return records.get(position);
    }


    /**
     * RecordViewHolder class
     */
    public class RecordViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView score_LBL_record;
        private final MaterialTextView score_LBL_name;
        private final AppCompatImageView score_IMG_location_icon;

        /**
         * RecordViewHolder constructor
         * @param itemView-View
         */
        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);

            score_LBL_record = itemView.findViewById(R.id.score_LBL_record);
            score_LBL_name = itemView.findViewById(R.id.score_LBL_name);
            score_IMG_location_icon = itemView.findViewById(R.id.score_IMG_location_icon);

            itemView.setOnClickListener(v -> mapsCallback.zoomOnMap(getItem(getAdapterPosition()), getAdapterPosition()));
        }
    }
}