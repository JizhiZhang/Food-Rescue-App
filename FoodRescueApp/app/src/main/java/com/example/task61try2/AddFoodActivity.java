package com.example.task61try2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task61try2.data.DatabaseHelper;
import com.example.task61try2.model.Food;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddFoodActivity extends AppCompatActivity {
    DatabaseHelper db;
    String imageLocation;
    ImageView foodImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        foodImageView = findViewById(R.id.foodImageView);
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        EditText pickupTimesEditText = findViewById(R.id.pickuptimesEditText);
        EditText quantityEditText = findViewById(R.id.quantityEditText);
        EditText locationEditText = findViewById(R.id.locationEditText);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Button addSaveButton = findViewById(R.id.addSaveButton);
        Button addImageButton = findViewById(R.id.addImageButton);
        db = new DatabaseHelper(this);

        addSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEditText.getText().toString().equals("") || descriptionEditText.getText().toString().equals("") ||
                        pickupTimesEditText.getText().toString().equals("") || quantityEditText.getText().toString().equals("")) {
                    Toast.makeText(AddFoodActivity.this, "Please enter all information!", Toast.LENGTH_SHORT).show();
                } else {
                    Food newFood = new Food();
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AddFoodActivity.this);
                    int user_id = sharedPref.getInt("CURRENT_USER", 0);
//                    int user_id = getIntent().getIntExtra("CURRENT_USER",0);
                    newFood.setUser_id(user_id);
                    newFood.setImage(imageLocation);
                    newFood.setTitle(titleEditText.getText().toString());
                    newFood.setDescription(descriptionEditText.getText().toString());
                    newFood.setDate(calendarView.getDate());
                    newFood.setPickUpTimes(pickupTimesEditText.getText().toString());
                    newFood.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                    newFood.setLocation(locationEditText.getText().toString());
                    long result = db.insertFood(newFood);
                    setResult(RESULT_OK, null);
                    finish();
                }
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AddFoodActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                            AddFoodActivity.this,
                            new String[]{
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            101);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 2);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageLocation = saveImage(this, selectedImage);
                File test = new File(imageLocation);
                Bitmap myBitmap = BitmapFactory.decodeFile(test.getAbsolutePath());
                foodImageView.setImageBitmap(myBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddFoodActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(AddFoodActivity.this, "Please choose a image!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImage(Context context, Bitmap image) {
        String savedImagePath = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/FoodRescue");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String savedMessage = "Saved";
        }
        return savedImagePath;
    }
}