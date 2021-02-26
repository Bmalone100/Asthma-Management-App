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
    //private ArrayList<Trigger> triggers;
    private String[] triggers;

    //public MyAdapter(ArrayList<Trigger> dataSet) {
        //triggers = dataSet;
    //}

    public MyAdapter(String[] dataSet) {
    triggers = dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rowView;

        public ViewHolder(final View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            rowView = view.findViewById(R.id.changeText);
        }

        /*public TextView getTextView() {
            return rowView;
        }*/
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View triggerView = inflater.inflate(R.layout.text_row_item, parent, false);
        // Return a new holder instance
        return new ViewHolder(triggerView);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        //for(int i = 0; i<triggers.size();i++) {
        //String trigger = triggers.get(position).getDescription();
        String trigger = triggers[position];
        Log.d(TAG, "onBindViewHolder: array element: " + triggers[position]);
            viewHolder.rowView.setText(trigger);
        //}

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return triggers.length;
    }
}
