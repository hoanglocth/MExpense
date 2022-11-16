package com.asm.m_expense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogConfirmFragment extends DialogFragment {
    private TextView nameTripDialog, destinationTripDialog, dateTripDialog, riskTripDialog, descriptionTripDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_confirm, null);
        // Get data from AddEditTripActivity
        String nameTrip = getArguments().getString("name");
        String destinationTrip = getArguments().getString("destination");
        String dateTrip = getArguments().getString("date");
        String riskTrip = getArguments().getString("risk");
        String descriptionTrip = getArguments().getString("description");
        // Init view dialog
        nameTripDialog = view.findViewById(R.id.nameTripDialog);
        destinationTripDialog = view.findViewById(R.id.destinationTripDialog);
        dateTripDialog = view.findViewById(R.id.dateTripDialog);
        riskTripDialog = view.findViewById(R.id.riskTripDialog);
        descriptionTripDialog = view.findViewById(R.id.descriptionTripDialog);
        // Set text by data received
        nameTripDialog.setText(nameTrip);
        destinationTripDialog.setText(destinationTrip);
        dateTripDialog.setText(dateTrip);
        riskTripDialog.setText(riskTrip);
        descriptionTripDialog.setText(descriptionTrip);

        dialog.setView(view)
                .setTitle("Confirm Data")
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    // Call createTrip() from AddEditTripActivity
                    ((AddEditTripActivity) getActivity()).createTrip();
                    // Move to new activity to display list trip
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                });
        return dialog.create();
    }
}