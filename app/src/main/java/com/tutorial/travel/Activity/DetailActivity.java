package com.tutorial.travel.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.tutorial.travel.DAOs.PopularCategoryDAO;
import com.tutorial.travel.Database.AppDatabase;
import com.tutorial.travel.Domain.CategoryDomain;
import com.tutorial.travel.Domain.PopularDomain;
import com.tutorial.travel.Helpers.ImageHelpers;
import com.tutorial.travel.R;

public class DetailActivity extends AppCompatActivity {
    private TextView placeNameTxt, locationTxt, detailDescriptionTxt, categoryTxt;
    private ImageView backBtn, picImg;
    private PopularDomain item;
    private AppCompatButton discoverBtn;
    private PopularCategoryDAO categoryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        categoryDao = AppDatabase.getInstance(this).categoryDAO();
        initView();
        setVariable();
    }

    private void setVariable() {
        item = (PopularDomain) getIntent().getSerializableExtra("object");

        placeNameTxt.setText(item.getPlaceName());
        locationTxt.setText(item.getLocation());
        detailDescriptionTxt.setText(item.getDetailDescription());

        CategoryDomain category = categoryDao.getCategoryById(item.getCategoryId());
        categoryTxt.setText(category.getTitle());

        Bitmap image = ImageHelpers.getImageFromStorage(this, item.getImageUrl());
        Glide.with(this)
                .asBitmap() // Indicate that we are loading a Bitmap
                .load(image)
                .transform(new CenterCrop(), new GranularRoundedCorners(40, 40, 40, 40))
                .into(picImg);

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(DetailActivity.this, MainActivity.class));
        });
    }

    private void initView() {
        placeNameTxt = findViewById(R.id.titleTxt);
        locationTxt = findViewById(R.id.locationTxt);
        detailDescriptionTxt = findViewById(R.id.descriptionTxt);
        categoryTxt = findViewById(R.id.categoryTxt);

        discoverBtn = findViewById(R.id.discoverButton);
        backBtn = findViewById(R.id.backBtn);
        picImg = findViewById(R.id.picImg);
    }
}