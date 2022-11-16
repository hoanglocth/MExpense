package com.asm.m_expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.asm.m_expense.adapters.AdapterTrip;
import com.asm.m_expense.database.Constants;
import com.asm.m_expense.database.DbHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    // View variables
    private FloatingActionMenu actionButton;
    private RecyclerView tripRecyclerView;
    private AdapterTrip adapterTrip;
    private SearchView searchTripView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init db
        dbHelper = new DbHelper(this);
        // Init view
        actionButton = findViewById(R.id.menu_up);
        FloatingActionButton addExpenseButton = findViewById(R.id.addExpense);
        FloatingActionButton addTripButton = findViewById(R.id.addTrip);
        tripRecyclerView = findViewById(R.id.tripRecycleView);
        searchTripView = findViewById(R.id.searchTrip);
        searchTripView.clearFocus();
        tripRecyclerView.setHasFixedSize(true);
        // Search listener
        searchTripView.setMaxWidth(Integer.MAX_VALUE);
        searchTripView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                searchTripView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                searchTrip(query);
                return false;
            }
        });

        // Click add trip button listener
        addTripButton.setOnClickListener(view -> {
            //Move to new activity to add trip
            Intent intent = new Intent(MainActivity.this, AddEditTripActivity.class);
            startActivity(intent);
            actionButton.close(true);
        });

        // Click add expense button listener
        addExpenseButton.setOnClickListener((view -> {
            //Move to new activity to add trip
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
            actionButton.close(true);
        }));

        // Call deleteTripById by swipe item left or right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Get id from setTag() in AdapterTrip
                String id = (String) viewHolder.itemView.getTag();
                deleteTripById(id);
                Toast.makeText(getApplicationContext(), "Deleted record", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(tripRecyclerView);
        // Display trip data
        readTrip();
    }

    // Delete trip by id query
    private void deleteTripById(String id) {
        // Get data from database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Delete by id query
        db.delete(Constants.TABLE_TRIP_NAME, Constants.C_ID_TRIP + "=" + id, null);
        db.execSQL("DELETE FROM " + Constants.TABLE_EXPENSE_NAME +
                " WHERE " + Constants.C_ID_TRIP_FK + "=" + id);
        onResume();
    }

    // Call function from dbHelper to display all trip on recycle view
    private void readTrip() {
        adapterTrip = new AdapterTrip(this, dbHelper.getAllTrip());
        tripRecyclerView.setAdapter(adapterTrip);
    }

    // Call function from dbHelper to search trip on recycle view
    private void searchTrip(String query) {
        adapterTrip = new AdapterTrip(this, dbHelper.searchTripByName(query));
        tripRecyclerView.setAdapter(adapterTrip);
    }

    // Init menu on main top
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllTrip) {
            dbHelper.deleteAllTrip();
            Toast.makeText(this, "Deleted all records", Toast.LENGTH_SHORT).show();
            onResume();
        } else if (item.getItemId() == R.id.syncData) {
            Toast.makeText(this, "Sync successfully", Toast.LENGTH_SHORT).show();
            onResume();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        readTrip();
        searchTripView.setQuery("", false);
        searchTripView.clearFocus();
    }
}