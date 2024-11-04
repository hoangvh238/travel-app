package com.tutorial.travel.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tutorial.travel.DAOs.PopularCategoryDAO;
import com.tutorial.travel.DAOs.PopularDAO;
import com.tutorial.travel.Database.AppDatabase;
import com.tutorial.travel.Domain.CategoryDomain;
import com.tutorial.travel.Domain.PopularDomain;
import com.tutorial.travel.Helpers.ImageHelpers;
import com.tutorial.travel.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CreatePopularActivity extends AppCompatActivity {
    private EditText placeNameEditText;
    private EditText locationEditText;
    private Spinner categorySpinner;
    private EditText detailedDescriptionEditText;
    private Button uploadImageButton, saveButton;
    private ImageView popularImageView;
    private FloatingActionButton backButton;

    private Bitmap currentImage;

    private PopularCategoryDAO categoryDAO;
    private PopularDAO popularDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_popular);

        categoryDAO = AppDatabase.getInstance(this).categoryDAO();
        popularDAO = AppDatabase.getInstance(this).popularDAO();

        initView();

        // Set listeners
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    submitForm();
                }
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to open image picker
                openGallery();
            }
        });
    }

    private void initView() {
        // Initialize views
        placeNameEditText = findViewById(R.id.placeNameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        detailedDescriptionEditText = findViewById(R.id.detailedDescriptionEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        saveButton = findViewById(R.id.saveButton);

        List<String> categories = categoryDAO.getCategoryNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);

        popularImageView = findViewById(R.id.popularImageView);

        backButton = findViewById(R.id.fab_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Validates the form inputs
     *
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateForm() {
        if (TextUtils.isEmpty(placeNameEditText.getText().toString())) {
            placeNameEditText.setError("Place name is required");
            return false;
        }
        if (TextUtils.isEmpty(locationEditText.getText().toString())) {
            locationEditText.setError("Location is required");
            return false;
        }
        if (TextUtils.isEmpty(detailedDescriptionEditText.getText().toString())) {
            detailedDescriptionEditText.setError("Detailed description is required");
            return false;
        }
        return true;
    }

    /**
     * Submits the form after validation
     */
    private void submitForm() {
        String imageUrl = "";
        String placeName = placeNameEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String detailedDescription = detailedDescriptionEditText.getText().toString();

        if(currentImage != null) {
            imageUrl = ImageHelpers.saveImageToLocal(currentImage, this);
        }

        CategoryDomain currentCategory = categoryDAO.getCategoryByTitle(category);
        popularDAO.insert(new PopularDomain(placeName, location, detailedDescription, imageUrl, currentCategory.getCategoryId()));
        Toast.makeText(this, "Form submitted successfully!", Toast.LENGTH_LONG).show();

        finish();
    }

    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickPhotoIntent.setType("image/*");
        uploadImageLauncher.launch(pickPhotoIntent);
    }
    ActivityResultLauncher<Intent> uploadImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // For gallery image
                        Uri selectedImageUri =  result.getData().getData();
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            runOnUiThread(() -> {
                                // Update UI elements here
                                showCurrentImage(imageBitmap);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void showCurrentImage(Bitmap bitmap) {
        currentImage = bitmap;
        popularImageView.setImageBitmap(currentImage);
    }
}

