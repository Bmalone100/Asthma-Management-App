package com.example.fypasthmaapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<String> triggers;
    private OnTriggerListener mOnTriggerListener;
    public MyAdapter(ArrayList<String> dataSet, OnTriggerListener onTriggerListener) {
        triggers = dataSet;
        this.mOnTriggerListener = onTriggerListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView rowView;
        OnTriggerListener onTriggerListener;

        public ViewHolder(final View view, OnTriggerListener onTriggerListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            rowView = view.findViewById(R.id.changeText);
            this.onTriggerListener = onTriggerListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTriggerListener.onTriggerClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View triggerView = inflater.inflate(R.layout.text_row_item, parent, false);
        // Return a new holder instance
        return new ViewHolder(triggerView, mOnTriggerListener);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String trigger = triggers.get(position);
        Log.d(TAG, "onBindViewHolder: array element: " + triggers.get(position));
            viewHolder.rowView.setText(trigger);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() { return triggers.size(); }

    public interface OnTriggerListener{
        void onTriggerClick(int position);
    }
}
