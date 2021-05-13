package com.example.task61try2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.task61try2.data.DatabaseHelper;
import com.example.task61try2.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements FoodViewAdapter.OnRowClickListener {
    DatabaseHelper db;
    PopupWindow popupWindow;
    List<Food> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView headerTextView = findViewById(R.id.headerTextView);
        ImageButton popupButton = findViewById(R.id.popupButton);
        db = new DatabaseHelper(this);
        foodList = db.fetchAllFood();
        RecyclerView foodRecyclerView = findViewById(R.id.foodRecyclerView);
        FoodViewAdapter foodViewAdapter = new FoodViewAdapter(foodList, HomeActivity.this, this);
        foodRecyclerView.setAdapter(foodViewAdapter);
        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        foodRecyclerView.setLayoutManager(layoutManagerFood);
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.popup_row, null);
                TextView homeTextView = view.findViewById(R.id.tv_1);
                TextView accountTextView = view.findViewById(R.id.tv_2);
                TextView myListTextView = view.findViewById(R.id.tv_3);

                homeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        headerTextView.setText("Discover free food");
                        db = new DatabaseHelper(HomeActivity.this);
                        foodList = db.fetchAllFood();

                        RecyclerView foodRecyclerView = findViewById(R.id.foodRecyclerView);
                        FoodViewAdapter foodViewAdapter = new FoodViewAdapter(foodList, HomeActivity.this, HomeActivity.this);
                        foodRecyclerView.setAdapter(foodViewAdapter);
                        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                        foodRecyclerView.setLayoutManager(layoutManagerFood);
                    }
                });
                accountTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                myListTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        headerTextView.setText("My List");
                        db = new DatabaseHelper(HomeActivity.this);
                        // 另一种尝试
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                        int user_id = sharedPref.getInt("CURRENT_USER", -1);
//                        int user_id = getIntent().getIntExtra("CURRENT_USER", 0);
                        foodList = db.fetchAllFood(user_id);
                        RecyclerView foodRecyclerView = findViewById(R.id.foodRecyclerView);
                        FoodViewAdapter foodViewAdapter = new FoodViewAdapter(foodList, HomeActivity.this, HomeActivity.this);
                        foodRecyclerView.setAdapter(foodViewAdapter);
                        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                        foodRecyclerView.setLayoutManager(layoutManagerFood);
                    }
                });

                popupWindow = new PopupWindow(view, 400, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(popupButton);
            }
        });

        FloatingActionButton addFabButton = findViewById(R.id.addFabButton);
        addFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(HomeActivity.this, AddFoodActivity.class);
                startActivityForResult(addIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, HomeActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    public void onItemClick(int position) {
    }

    public void onShareButtonClick(int position) {
        Food selectedFood = foodList.get(position);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Come and get it! - " + selectedFood.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Come and rescue this food before it goes to waste! "
                + "\n" + selectedFood.getTitle()
                + "\n" + selectedFood.getDescription()
                + "\nDownload Food Rescue to see the full details");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}