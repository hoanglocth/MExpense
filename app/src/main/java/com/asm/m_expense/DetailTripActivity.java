package com.asm.m_expense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.asm.m_expense.adapters.AdapterExpense;

import com.asm.m_expense.database.Constants;
import com.asm.m_expense.database.DbHelper;


public class DetailTripActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    // View variables
    private TextView nameTripDetail, destinationTripDetail, dateTripDetail, riskTripDetail, descriptionTripDetail;
    private String id;
    private AdapterExpense adapterExpense;
    private RecyclerView expenseRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);
        // Init db
        dbHelper = new DbHelper(this);
        // Get data from intent
        Intent intent = getIntent();
        id = intent.getStringExtra("tripId");
        // Init view
        nameTripDetail = findViewById(R.id.nameTripDetail);
        destinationTripDetail = findViewById(R.id.destinationTripDetail);
        dateTripDetail = findViewById(R.id.dateTripDetail);
        riskTripDetail = findViewById(R.id.riskTripDetail);
        descriptionTripDetail = findViewById(R.id.descriptionTripDetail);
        expenseRecycleView = findViewById(R.id.expenseRecycleView);
        expenseRecycleView.setHasFixedSize(true);
        Button editTripButton = findViewById(R.id.editTripButton);
        // Click edit button listener
        editTripButton.setOnClickListener(view -> {
            // Move to new activity to edit list trip
            Intent intent1 = new Intent(DetailTripActivity.this, AddEditTripActivity.class);
            // Get data from current trip
            String getName = nameTripDetail.getText().toString();
            String getDestination = destinationTripDetail.getText().toString();
            String getDate = dateTripDetail.getText().toString();
            String getRisk = riskTripDetail.getText().toString();
            String getDescription = descriptionTripDetail.getText().toString();
            // Send data to AddEditTripActivity
            intent1.putExtra("id", id);
            intent1.putExtra("name", getName);
            intent1.putExtra("destination", getDestination);
            intent1.putExtra("date", getDate);
            intent1.putExtra("risk", getRisk);
            intent1.putExtra("description", getDescription);
            // Set isEditMode = true to init edit mode
            intent1.putExtra("isEditMode", true);
            startActivity(intent1);
        });
        readTripById();
        readExpenseByIdTrip(id);
    }

    private void readTripById() {
        // Get data from database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Query for find data by id
        String selectQuery = "SELECT * FROM "+ Constants.TABLE_TRIP_NAME + " WHERE " + Constants.C_ID_TRIP + " =\"" + id + "\"";
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Lopping through all records to read from list then display data on ui
        if (cursor.moveToFirst())
        {
            do {
                String name = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME_TRIP));
                String destination = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DESTINATION_TRIP));
                String date = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DATE_TRIP));
                String risk = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_RISK_TRIP));
                String description = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DESCRIPTION_TRIP));
                // Set data
                nameTripDetail.setText(name);
                destinationTripDetail.setText(destination);
                dateTripDetail.setText(date);
                riskTripDetail.setText(risk);
                descriptionTripDetail.setText(description);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    private void readExpenseByIdTrip(String id) {
        adapterExpense = new AdapterExpense(this, dbHelper.readByIdTrip(id));
        expenseRecycleView.setAdapter(adapterExpense);
    }
}