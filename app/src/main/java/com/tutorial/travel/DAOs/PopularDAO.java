package com.tutorial.travel.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tutorial.travel.Domain.PopularDomain;

import java.util.List;

@Dao
public interface PopularDAO {
    // Insert data
    @Insert
    void insert(PopularDomain popular);

    // Update data
    @Update
    void update(PopularDomain popular);

    // Delete data
    @Delete
    void delete(PopularDomain popular);

    @Query("DELETE FROM populars")
    void deleteAll();

    // Get all data
    @Query("SELECT * FROM populars")
    List<PopularDomain> getAllPopulars();

    // Get data by id
    @Query("SELECT * FROM populars WHERE id = :id")
    PopularDomain getPopularById(int id);
}
