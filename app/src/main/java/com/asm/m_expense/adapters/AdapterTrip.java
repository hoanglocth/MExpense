package com.asm.m_expense.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asm.m_expense.database.models.ModelTrip;
import com.asm.m_expense.R;
import com.asm.m_expense.DetailTripActivity;
import java.util.ArrayList;

public class AdapterTrip extends RecyclerView.Adapter<AdapterTrip.TripViewHolder> {
    private final Context context;
    private final ArrayList<ModelTrip> tripList;

    // Add constructor
    public AdapterTrip(Context context, ArrayList<ModelTrip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_trip_item, parent, false);
        return new TripViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        ModelTrip modelTrip = tripList.get(position);
        // Get data from sqlite
        String id = modelTrip.getId();
        String name = modelTrip.getName();
        String destination = modelTrip.getDestination();
        String date = modelTrip.getDate();
        // Set data in ui
        holder.itemView.setTag(id);
        holder.tripName.setText(name);
        holder.tripDestination.setText("To: " + destination);
        holder.tripDate.setText(date);
        // Handle click on trip item to show details
        holder.layoutTripItem.setOnClickListener(view -> {
            // Move to DetailTripActivity and send id data to new intent
            Intent intent = new Intent(context, DetailTripActivity.class);
            intent.putExtra("tripId", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    // ViewHolder trip
    static class TripViewHolder extends RecyclerView.ViewHolder {
        private final TextView tripName;
        private final TextView tripDestination;
        private final TextView tripDate;
        private final RelativeLayout layoutTripItem;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            // Init view
            layoutTripItem = itemView.findViewById(R.id.layoutTripItem);
            tripName = itemView.findViewById(R.id.nameTripRV);
            tripDestination = itemView.findViewById(R.id.destinationTripRV);
            tripDate = itemView.findViewById(R.id.dateTripRV);
        }
    }
}
