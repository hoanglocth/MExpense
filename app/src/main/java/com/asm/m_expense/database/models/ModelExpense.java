package com.asm.m_expense.database.models;

import java.text.DecimalFormat;

public class ModelExpense {
    private String id;
    private String type;
    private String amount;
    private String time;
    private String comment;
    private String idTrip;

    public ModelExpense(String id, String type, String amount, String time, String comment, String idTrip) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.comment = comment;
        this.idTrip = idTrip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String amountCurrency(String amount)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(Double.parseDouble(amount));
    }

    public String getAmount() {
        return amountCurrency(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(String idTrip) {
        this.idTrip = idTrip;
    }

}
