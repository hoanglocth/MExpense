package com.asm.m_expense.database;

public class Constants {
    // Init db
    public static final String DATABASE_NAME = "M_DB";
    public static final int DATABASE_VERSION = 1;

    // Init table name
    public static final String TABLE_TRIP_NAME = "TRIP_TABLE";
    public static final String TABLE_EXPENSE_NAME = "EXPENSE_TABLE";
    // Field table trip
    public static final String C_ID_TRIP = "ID_TRIP";
    public static final String C_NAME_TRIP = "NAME_TRIP";
    public static final String C_DESTINATION_TRIP = "DESTINATION_TRIP";
    public static final String C_DATE_TRIP = "DATE_TRIP";
    public static final String C_RISK_TRIP = "RISK_TRIP";
    public static final String C_DESCRIPTION_TRIP = "DESCRIPTION_TRIP";
    // Field table expense
    public static final String C_ID_EXPENSE = "ID_EXPENSE" ;
    public static final String C_ID_TRIP_FK = "ID_TRIP_FK" ;
    public static final String C_TYPE_EXPENSE = "TYPE_EXPENSE";
    public static final String C_TIME_EXPENSE = "TIME_EXPENSE";
    public static final String C_AMOUNT_EXPENSE = "AMOUNT_EXPENSE";
    public static final String C_COMMENT_EXPENSE = "COMMENT_EXPENSE";
    // Create table trip query
    public static final String CREATE_TRIP_TABLE = "CREATE TABLE " +  TABLE_TRIP_NAME + "( "
            + C_ID_TRIP +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_NAME_TRIP + " TEXT, "
            + C_DESTINATION_TRIP + " TEXT, "
            + C_DATE_TRIP + " TEXT, "
            + C_RISK_TRIP + "  TEXT, "
            + C_DESCRIPTION_TRIP + " TEXT"
            +" );";
    // Create table expense query
    public static final String CREATE_EXPENSE_TABLE = "CREATE TABLE " +  TABLE_EXPENSE_NAME + "( "
            + C_ID_EXPENSE +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_TYPE_EXPENSE + " TEXT, "
            + C_AMOUNT_EXPENSE + " TEXT, "
            + C_TIME_EXPENSE + " TEXT, "
            + C_COMMENT_EXPENSE + " TEXT, "
            + C_ID_TRIP_FK + " INTEGER REFERENCES " + TABLE_TRIP_NAME + "(" + C_ID_TRIP + ")"
            +" );";

}
