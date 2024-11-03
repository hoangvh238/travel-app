package com.tutorial.travel.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tutorial.travel.Domain.PopularDomain;
import com.tutorial.travel.R;

public class DetailActivity extends AppCompatActivity {
    private TextView placeNameTxt, locationTxt, shortDescriptionTxt, detailDescriptionTxt, imageUrlTxt;
    private ImageView backBtn, picImg;
    private PopularDomain item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        setVariable();
    }

    private void setVariable() {
        item = (PopularDomain) getIntent().getSerializableExtra("object");

        placeNameTxt.setText(item.getPlaceName());
        locationTxt.setText(item.getLocation());
        shortDescriptionTxt.setText(item.getShortDescription());
        detailDescriptionTxt.setText(item.getDetailDescription());

        int drawableResourceId = getResources().getIdentifier(item.getImageUrl(), "drawable", getPackageName());

        Glide.with(this)
                .load(drawableResourceId)
                .into(picImg);

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(DetailActivity.this, MainActivity.class));
        });

    }

    private void initView() {
        placeNameTxt = findViewById(R.id.titleTxt);
        locationTxt = findViewById(R.id.locationTxt);
        shortDescriptionTxt = findViewById(R.id.scoreTxt);
        detailDescriptionTxt = findViewById(R.id.bedTxt);
        imageUrlTxt = findViewById(R.id.guideTxt);

        backBtn = findViewById(R.id.backBtn);
        picImg = findViewById(R.id.picImg);
    }
}