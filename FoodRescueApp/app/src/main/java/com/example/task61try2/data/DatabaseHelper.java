package com.example.task61try2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.task61try2.model.Food;
import com.example.task61try2.model.User;
import com.example.task61try2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME_USER + "(" + Util.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.NAME + " TEXT, "
                + Util.EMAIL + " TEXT, "
                + Util.PHONE + " TEXT, "
                + Util.ADDRESS + " TEXT, "
                + Util.PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_FOOD_TABLE = "CREATE TABLE " + Util.TABLE_NAME_FOOD + "(" + Util.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.USER_ID_FOOD + " INTEGER,"
                + Util.IMAGE + " TEXT, "
                + Util.TITLE + " TEXT, "
                + Util.DESCRIPTION + " TEXT, "
                + Util.DATE + " INTEGER, "
                + Util.PICK_UP_TIMES + " INTEGER, "
                + Util.QUANTITY + " INTEGER, "
                + Util.LOCATION + " TEXT)";
        db.execSQL(CREATE_FOOD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_USER_TABLE = " DROP TABLE IF EXISTS ";
        db.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME_USER});
        db.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME_FOOD});
        onCreate(db);
    }

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.NAME, user.getName());
        contentValues.put(Util.EMAIL, user.getEmail());
        contentValues.put(Util.PHONE, user.getPhone());
        contentValues.put(Util.ADDRESS, user.getAddress());
        contentValues.put(Util.PASSWORD, user.getPassword());
        long newRowId = db.insert(Util.TABLE_NAME_USER, null, contentValues);
        db.close();
        return newRowId;
    }

    public int fetchUser(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_USER, new String[]{Util.USER_ID}, Util.NAME + "=? and " + Util.PASSWORD + "=?",
                new String[]{name, password}, null, null, null);

        if (cursor.moveToFirst()) {
            db.close();
            return cursor.getInt(cursor.getColumnIndex(Util.USER_ID));
        } else {
            db.close();
            return -1;
        }
    }

    public long insertFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USER_ID_FOOD, food.getUser_id());
        contentValues.put(Util.IMAGE, food.getImage());
        contentValues.put(Util.TITLE, food.getTitle());
        contentValues.put(Util.DESCRIPTION, food.getDescription());
        contentValues.put(Util.DATE, food.getDate());
        contentValues.put(Util.PICK_UP_TIMES, food.getPickUpTimes());
        contentValues.put(Util.QUANTITY, food.getQuantity());
        contentValues.put(Util.LOCATION, food.getLocation());
        long newRowId = db.insert(Util.TABLE_NAME_FOOD, null, contentValues);
        db.close();
        return newRowId;
    }

    public List<Food> fetchAllFood() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAll = " SELECT * FROM " + Util.TABLE_NAME_FOOD;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setUser_id(cursor.getInt(cursor.getColumnIndex(Util.USER_ID)));
                food.setImage(cursor.getString(cursor.getColumnIndex(Util.IMAGE)));
                food.setTitle(cursor.getString(cursor.getColumnIndex(Util.TITLE)));
                food.setDescription(cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION)));
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        return foodList;
    }

    public List<Food> fetchAllFood(int userId) {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_FOOD + " WHERE " + Util.USER_ID + "= ?";
        Cursor cursor = db.rawQuery(selectAll, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setUser_id((cursor.getInt(cursor.getColumnIndex(Util.USER_ID))));
                food.setImage(cursor.getString(cursor.getColumnIndex(Util.IMAGE)));
                food.setTitle(cursor.getString(cursor.getColumnIndex(Util.TITLE)));
                food.setDescription(cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION)));
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        return foodList;
    }
}
