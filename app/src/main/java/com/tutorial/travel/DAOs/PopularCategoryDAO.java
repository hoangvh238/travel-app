package com.tutorial.travel.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tutorial.travel.Domain.CategoryDomain;

import java.util.List;

@Dao
public interface PopularCategoryDAO {
    // Insert
    @Insert
    void insertCategory(CategoryDomain category);

    // Delete all categories
    @Query("DELETE FROM popularCategories")
    void deleteAll();

    // Get all data
    @Query("SELECT * FROM popularCategories")
    List<CategoryDomain> getAllCategories();

    // Get data by id
    @Query("SELECT * FROM popularCategories WHERE categoryId = :id")
    CategoryDomain getCategoryById(int id);
}

