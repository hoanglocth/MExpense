package com.asm.m_expense;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asm.m_expense.database.DbHelper;
import com.asm.m_expense.database.models.ModelTrip;

import java.util.ArrayList;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    // View variables
    private String type, amount, time, comment, idTrip;
    private EditText typeExpense, amountExpense, commentExpense;
    private Spinner tripSpinner;
    private DatePickerDialog datePickerDialog;
    private Button timeExpense;
    private ArrayList<ModelTrip> tripArrayList;
    private boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        // Init db
        dbHelper = new DbHelper(this);
        // Init actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add New Expense");
        }
        // Init view
        timeExpense = findViewById(R.id.timeExpense);
        timeExpense.setText(getTodayDate());
        typeExpense = findViewById(R.id.typeExpense);
        amountExpense = findViewById(R.id.amountExpense);
        commentExpense = findViewById(R.id.commentExpense);
        tripSpinner = findViewById(R.id.tripSpinner);
        Button saveExpenseButton = findViewById(R.id.saveExpenseButton);

        initTripSpinner();
        initDatePicker();
        // Click spinner button listener
        tripSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                // Get id of selected trip from spinner
                idTrip = tripArrayList.get(tripSpinner.getSelectedItemPosition()).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Click save button listener
        saveExpenseButton.setOnClickListener(view -> {
            // store the returned value of the dedicated function which checks
            // whether the entered data is valid or if any fields are left blank.
            isAllFieldsChecked = CheckAllFields();
            // the boolean variable turns to be true then
            // only the user must be proceed to saveData()
            if (isAllFieldsChecked) {
                createExpense();
                //Move to new activity to display list trip
                Intent intent1 = new Intent(AddExpenseActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }

    // Create Expense with id trip fk
    private void createExpense() {
        type = typeExpense.getText().toString();
        amount = amountExpense.getText().toString();
        // Get string from startIndex = 6 ( Except Date: )
        time = timeExpense.getText().toString().substring(6);
        comment = commentExpense.getText().toString();
        dbHelper.insertExpense(
                "" + type,
                "" + amount,
                "" + time,
                "" + comment,
                "" + idTrip
        );
        Toast.makeText(getApplicationContext(), "Expense created successfully", Toast.LENGTH_SHORT).show();
    }

    // Validate all fields
    private boolean CheckAllFields() {
        if (typeExpense.length() == 0) {
            typeExpense.setError("This field is required");
            return false;
        }

        if (amountExpense.length() == 0) {
            amountExpense.setError("This field is required");
            return false;
        }

        if (idTrip == null) {
            Toast.makeText(getApplicationContext(), "Id trip is empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Initialize spinner trip menu
    private void initTripSpinner() {
        // Call getAllTrip from db
        tripArrayList = dbHelper.getAllTrip();
        // Spinner Drop down elements as name trip
        ArrayList<String> nameTripList = new ArrayList<>();
        for (ModelTrip modelTrip : tripArrayList) {
            nameTripList.add(modelTrip.getName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nameTripList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripSpinner.setAdapter(dataAdapter);
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

    // Initialize date of trip is current time as default
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            timeExpense.setText(date);
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