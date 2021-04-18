package com.example.fypasthmaapp.symptoms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypasthmaapp.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

class MySymptomAdapter extends RecyclerView.Adapter<MySymptomAdapter.ViewHolder>{
    ArrayList<String> symptoms;
    String currentDate = "";
    SQLiteDatabase db;

    /**
     * Controls screen contents
     */
    public MySymptomAdapter(ArrayList<String> dataSet) {
        symptoms = dataSet;
    }

    /**
     * Holds the contents of the view/screen component
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView element;
        private TextView date;
        private Button deleteBtn;

        public ViewHolder(final View view) {
            super(view);
            //Define click listener for the ViewHolder's View
            element = view.findViewById(R.id.element);
            date = view.findViewById(R.id.dateOfElement);
            deleteBtn = view.findViewById(R.id.deleteEntry);
        }

    }

    /**
     * Inflate the layout from a predefined xml file
     */
    @NonNull
    @Override
    public MySymptomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View symptomView = inflater.inflate(R.layout.text_row_item_status, parent, false);
        // Return a new holder instance
        return new MySymptomAdapter.ViewHolder(symptomView);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(MySymptomAdapter.ViewHolder viewHolder, int position) {
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime());
        // Get the data model based on position
        String text = symptoms.get(position);
        Log.d(TAG, "onBindViewHolder: array element: " + symptoms.get(position));
        viewHolder.element.setText(text);
        viewHolder.date.setText(currentDate);

        viewHolder.deleteBtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Clicked bind 2");
            //Add db delete
            String change = viewHolder.element.getText().toString();
            db = (new SymptomDbHelper(v.getContext())).getWritableDatabase();
            String query = "SELECT * From symptoms WHERE symptom = ?";
            Cursor csr = db.rawQuery(query, new String[]{change});
            csr.moveToFirst();
            String symp = csr.getString(csr.getColumnIndex("symptom"));
            db.delete("symptoms", "symptom = ? ", new String[] { symp } );
            Log.d(TAG, "onClick: Deleted entry: " + change);
            removeItem(position);
            csr.close();
            db.close();
        });
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return symptoms.size();
    }

    /**
     * Remove an item from the display
     */
    public void removeItem(int position) {
        if(!(symptoms.isEmpty())) {
            symptoms.remove(position);
            notifyItemRemoved(position);
        }else{
            Log.d(TAG, "removeItem: No items to remove");
        }
    }

}
