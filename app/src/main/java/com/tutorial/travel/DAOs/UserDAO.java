package com.tutorial.travel.DAOs;

import com.tutorial.travel.Entity.User;
import androidx.room.*;
import java.util.List;

@Dao
public interface UserDAO {
    // Insert data
    @Insert
    void insert(User user);

    // Update data
    @Update
    void update(User user);

    // Delete data
    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    // Get all data
    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    // Get data by id
    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(String id);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);
}
