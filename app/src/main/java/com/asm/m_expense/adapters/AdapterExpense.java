package com.asm.m_expense.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asm.m_expense.R;
import com.asm.m_expense.database.models.ModelExpense;

import java.util.ArrayList;

public class AdapterExpense extends RecyclerView.Adapter<AdapterExpense.ExpenseViewHolder> {
    private final Context context;
    private final ArrayList<ModelExpense> expenseList;

    // Add constructor
    public AdapterExpense(Context context, ArrayList<ModelExpense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public AdapterExpense.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterExpense.ExpenseViewHolder holder, int position) {
        ModelExpense modelExpense = expenseList.get(position);
        // Get data from sqlite
        String type = modelExpense.getType();
        String amount = modelExpense.getAmount();
        String time = modelExpense.getTime();
        String comment = modelExpense.getComment();
        // Set data in ui
        holder.expenseType.setText("Type: " + type);
        holder.expenseAmount.setText("Amount: $" + amount);
        holder.expenseTime.setText(time);
        holder.expenseComment.setText(comment);
    }


    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ViewHolder Expense
    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView expenseType;
        private final TextView expenseAmount;
        private final TextView expenseTime;
        private final TextView expenseComment;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            // Init view
            expenseType = itemView.findViewById(R.id.typeExpense);
            expenseAmount = itemView.findViewById(R.id.amountExpense);
            expenseTime = itemView.findViewById(R.id.timeExpense);
            expenseComment = itemView.findViewById(R.id.commentExpense);
        }
    }
}
