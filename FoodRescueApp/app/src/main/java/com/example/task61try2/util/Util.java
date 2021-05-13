package com.example.task61try2.util;

public class Util {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "food_rescue_db";

    // User table
    public static final String TABLE_NAME_USER = "user";
    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String PASSWORD = "password";

    // Food table
    public static final String TABLE_NAME_FOOD = "food";
    public static final String FOOD_ID = "food_id";
    public static final String USER_ID_FOOD = "user_id";
    public static final String IMAGE = "image";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String PICK_UP_TIMES = "pick_up_times";
    public static final String QUANTITY = "quantity";
    public static final String LOCATION = "location";
}
