package com.tutorial.travel.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorial.travel.Adapter.CategoryAdapter;
import com.tutorial.travel.Adapter.PopularAdapter;
import com.tutorial.travel.DAOs.PopularCategoryDAO;
import com.tutorial.travel.DAOs.PopularDAO;
import com.tutorial.travel.Database.AppDatabase;
import com.tutorial.travel.Domain.CategoryDomain;
import com.tutorial.travel.Domain.PopularDomain;
import com.tutorial.travel.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PopularDAO popularDAO;
    private PopularCategoryDAO categoryDao;
    private RecyclerView.Adapter adapterPopular, adapterCat;
    private RecyclerView recyclerViewPopular, recyclerViewCategory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryDao = AppDatabase.getInstance(this).categoryDAO();
        popularDAO = AppDatabase.getInstance(this).popularDAO();
        initRecyclerView();
    }

    private void initRecyclerView() {
        // For Popular RecyclerView
            List<PopularDomain> items = popularDAO.getAllPopulars();

        recyclerViewPopular = findViewById(R.id.recyclerview1);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        ));
        adapterPopular = new PopularAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);

        // For Category RecyclerView
        List<CategoryDomain> catsList = categoryDao.getAllCategories();
        recyclerViewCategory = findViewById(R.id.recyclerview2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        ));

        adapterCat = new CategoryAdapter(catsList);
        recyclerViewCategory.setAdapter(adapterCat);
    }
}
