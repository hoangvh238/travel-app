package com.tutorial.travel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tutorial.travel.Adapter.CategoryAdapter;
import com.tutorial.travel.Adapter.PopularAdapter;
import com.tutorial.travel.DAOs.PopularCategoryDAO;
import com.tutorial.travel.DAOs.PopularDAO;
import com.tutorial.travel.Database.AppDatabase;
import com.tutorial.travel.Domain.CategoryDomain;
import com.tutorial.travel.Domain.PopularDomain;
import com.tutorial.travel.Domain.UserAccount;
import com.tutorial.travel.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PopularDAO popularDAO;
    private PopularCategoryDAO categoryDao;
    private RecyclerView.Adapter adapterPopular, adapterCat;
    private RecyclerView recyclerViewPopular, recyclerViewCategory;
    FloatingActionButton addButton;
    TextView userNameText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryDao = AppDatabase.getInstance(this).categoryDAO();
        popularDAO = AppDatabase.getInstance(this).popularDAO();

        initGeneralView();
        UserAccount user = (UserAccount) getIntent().getSerializableExtra("user");
        // set general information into view
        if (user != null) {
            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                addButton.setVisibility(View.VISIBLE);
            } else {
                addButton.setVisibility(View.GONE);
            }
            userNameText.setText(user.getFullName());
        }
        initRecyclerView();
    }

    private void initGeneralView() {
        userNameText = findViewById(R.id.tvUserName);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreatePopularActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        // For Popular RecyclerView
        List<PopularDomain> items = popularDAO.getAllPopulars();

        recyclerViewPopular = findViewById(R.id.recyclerview1);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPopular = new PopularAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);

        // For Category RecyclerView
        List<CategoryDomain> catsList = categoryDao.getAllCategories();
        recyclerViewCategory = findViewById(R.id.recyclerview2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterCat = new CategoryAdapter(catsList);
        recyclerViewCategory.setAdapter(adapterCat);

        registerForContextMenu(recyclerViewPopular);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getGroupId(); // Get the adapter position

        if (item.getTitle().equals("Delete")) {
            // Perform the delete action here
            deletePopularByIndex(position);

            // Notify success delete
            Toast.makeText(this, "Delete popular successfully!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPopulars();
    }

    private void deletePopularByIndex(int index){
        List<PopularDomain> items = popularDAO.getAllPopulars();
        PopularDomain popular = items.get(index);
        popularDAO.delete(popular);

        // Refresh after deleting
        refreshPopulars();
    }

    private void refreshPopulars() {
        List<PopularDomain> items = popularDAO.getAllPopulars();
        adapterPopular = new PopularAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);
    }
}
