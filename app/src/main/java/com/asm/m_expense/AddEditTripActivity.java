package com.asm.m_expense;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.asm.m_expense.database.DbHelper;

import java.util.Calendar;

public class AddEditTripActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    // View variables
    private DatePickerDialog datePickerDialog;
    private Button dateTrip;
    private EditText nameTrip, destinationTrip, descriptionTrip;
    private CheckedTextView riskTripCheck;
    private String id, name, date, destination, risk = "No", description;
    private Boolean isEditMode;
    private boolean isAllFieldsChecked = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        // Init db
        dbHelper = new DbHelper(this);
        // Init view
        nameTrip = findViewById(R.id.nameTrip);
        destinationTrip = findViewById(R.id.destinationTrip);
        dateTrip = findViewById(R.id.dateTrip);
        dateTrip.setText(getTodayDate());
        riskTripCheck = findViewById(R.id.riskTripCheck);
        descriptionTrip = findViewById(R.id.descriptionTrip);
        Button saveTripButton = findViewById(R.id.saveTripButton);
        // Init actionbar
        ActionBar actionBar = getSupportActionBar();
        // Get intent data
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        // Check edit mode or add mode
        if (isEditMode) { // Set title edit trip and init data
            if (actionBar != null) {
                actionBar.setTitle("Edit Trip Information");
            }
            // Receive data from DetailTripActivity
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            destination = intent.getStringExtra("destination");
            date = intent.getStringExtra("date");
            risk = intent.getStringExtra("risk");
            description = intent.getStringExtra("description");
            // Set value in fields
            nameTrip.setText(name);
            destinationTrip.setText(destination);
            dateTrip.setText("DATE: " + date);
            descriptionTrip.setText(description);
            // Set checkbox
            riskTripCheck.setChecked(risk.equals("Yes"));
        } else { // Set title add new trip
            if (actionBar != null) {
                actionBar.setTitle("Add New Trip");
            }
        }
        // Init view
        initDatePicker();
        initRiskAssessment();
        // Click save button listener
        saveTripButton.setOnClickListener(view -> {
            // store the returned value of the dedicated function which checks
            // whether the entered data is valid or if any fields are left blank.
            isAllFieldsChecked = CheckAllFields();
            // the boolean variable turns to be true then
            // only the user must be proceed to saveData()
            if (isAllFieldsChecked) {
                name = nameTrip.getText().toString();
                destination = destinationTrip.getText().toString();
                // Get string from startIndex = 6 ( Except Date: )
                date = dateTrip.getText().toString().substring(6);
                description = descriptionTrip.getText().toString();
                // Put data from this activity to fragment dialog
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("destination", destination);
                bundle.putString("date", date);
                bundle.putString("risk", risk);
                bundle.putString("description", description);
                // Open dialog fragment to confirm data
                DialogConfirmFragment dialogConfirmFragment = new DialogConfirmFragment();
                dialogConfirmFragment.setArguments(bundle);
                dialogConfirmFragment.show(getSupportFragmentManager(), "dialog");
            }
        });

    }

    // Validate all fields
    private boolean CheckAllFields() {
        if (nameTrip.length() == 0) {
            nameTrip.setError("This field is required");
            return false;
        }

        if (destinationTrip.length() == 0) {
            destinationTrip.setError("This field is required");
            return false;
        }

        if (dateTrip.length() == 0) {
            dateTrip.setError("This field is required");
            return false;
        }
        return true;
    }

    // CreateTrip
    public void createTrip() {
        name = nameTrip.getText().toString();
        destination = destinationTrip.getText().toString();
        // Get string from startIndex = 6 ( Except Date: )
        date = dateTrip.getText().toString().substring(6);
        description = descriptionTrip.getText().toString();
        if (isEditMode) { // Function edit trip
            dbHelper.updateTrip(
                    "" + id,
                    "" + name,
                    "" + destination,
                    "" + date,
                    "" + risk,
                    "" + description
            );
            Toast.makeText(getApplicationContext(), "Trip updated successfully", Toast.LENGTH_SHORT).show();
        } else { // Function add trip
            dbHelper.insertTrip(
                    "" + name,
                    "" + destination,
                    "" + date,
                    "" + risk,
                    "" + description
            );
            Toast.makeText(getApplicationContext(), "Trip inserted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    // Get today date function
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    // Initialize requires risk assessment is false as default
    private void initRiskAssessment() {
        riskTripCheck.setOnClickListener(view -> {
            riskTripCheck.toggle();
            if (riskTripCheck.isChecked()) {
                risk = "Yes";
                riskTripCheck.setChecked(true);
            } else {
                risk = "No";
                riskTripCheck.setChecked(false);
            }
        });
    }

    // Initialize date of trip is current time as default
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            dateTrip.setText(date);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = android.R.style.Theme_Material_Dialog_Alert;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    // Display date string on ui
    private String makeDateString(int day, int month, int year) {
        return "Date: " + day + "/" + month + "/" + year;
    }

    // Open date pick dialog
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}