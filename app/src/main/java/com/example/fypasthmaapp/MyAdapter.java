package com.example.fypasthmaapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<String> triggers;
    String message = "";
    SQLiteDatabase db;
    triggerDbHelper dbHelper;

    public MyAdapterListener onClickListener;

    public MyAdapter(ArrayList<String> dataSet, MyAdapterListener listener) {
        triggers = dataSet;
        onClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rowView;
        private Button editBtn;
        private Button deleteBtn;
        private EditText editMe;

        public ViewHolder(final View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            rowView = view.findViewById(R.id.changeText);
            editBtn = view.findViewById(R.id.editTrigger);
            editBtn.setVisibility(View.VISIBLE);
            deleteBtn = view.findViewById(R.id.deleteTrigger);
            editMe = view.findViewById(R.id.anEditText);
            editMe.setVisibility(View.GONE);

            editBtn.setOnClickListener(v -> onClickListener.editBtnOnClick(v, getAdapterPosition()));
            deleteBtn.setOnClickListener(v -> onClickListener.deleteBtnOnClick(v, getAdapterPosition()));
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
        return new ViewHolder(triggerView);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String trigger = triggers.get(position);
        Log.d(TAG, "onBindViewHolder: array element: " + triggers.get(position));
            viewHolder.rowView.setText(trigger);

        viewHolder.editBtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Clicking bind 1");
            viewHolder.editBtn.setVisibility(View.GONE);
            viewHolder.editMe.setVisibility(View.VISIBLE);
        });

        viewHolder.editMe.setOnKeyListener(((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //Add db update
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String change = viewHolder.rowView.getText().toString();
                String insert = viewHolder.editMe.getText().toString();

                //db = (new triggerDbHelper(v.getContext())).getReadableDatabase();
                db = (new triggerDbHelper(v.getContext())).getWritableDatabase();
                String query = "SELECT * From triggers WHERE trg = ?";
                Log.d(TAG, "onKey: query returns: " + query);
                Cursor csr = db.rawQuery(query, new String[]{change});
                csr.moveToFirst();
                String trg = csr.getString(csr.getColumnIndex("trg"));

                //db = (new triggerDbHelper(v.getContext())).getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("user", username);
                cv.put("trg", change);
                db.update("triggers", cv, "trg = ? ", new String[] { trg } );
                csr.close();
                db.close();
                Log.d(TAG, "Db Update: updated: " + change + "with: " + insert);

                message = viewHolder.editMe.getText().toString();
                triggers.set(position, message);

                viewHolder.rowView.setText(triggers.get(position));

                viewHolder.editMe.setVisibility(View.GONE);
                viewHolder.editBtn.setVisibility(View.VISIBLE);
            }
            return false;
        }));

        viewHolder.deleteBtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Clicked bind 2");
            //Add db delete
            String change = viewHolder.rowView.getText().toString();
            db = (new triggerDbHelper(v.getContext())).getWritableDatabase();
            String query = "SELECT * From triggers WHERE trg = ?";
            Cursor csr = db.rawQuery(query, new String[]{change});
            csr.moveToFirst();
            String trg = csr.getString(csr.getColumnIndex("trg"));
            db.delete("triggers", "trg = ? ", new String[] { trg } );
            Log.d(TAG, "onClick: Deleted entry: " + change);
            removeItem(position);
            csr.close();
            db.close();
           });
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return triggers.size();
    }

    public void removeItem(int position) {
        if(!(triggers.isEmpty())) {
            triggers.remove(position);
            notifyItemRemoved(position);
        }else{
            Log.d(TAG, "removeItem: No items to remove");
        }
    }

    public interface MyAdapterListener {

        void editBtnOnClick(View v, int position);
        void deleteBtnOnClick(View v, int position);
    }
}
