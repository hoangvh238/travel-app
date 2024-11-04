package com.tutorial.travel.DAOs;

import com.tutorial.travel.Domain.UserAccount;
import androidx.room.*;
import java.util.List;

@Dao
public interface UserAccountDAO {
    // Insert data
    @Insert
    void insert(UserAccount user);

    // Update data
    @Update
    void update(UserAccount user);

    // Delete data
    @Delete
    void delete(UserAccount user);

    @Query("DELETE FROM userAccounts")
    void deleteAll();

    // Get all data
    @Query("SELECT * FROM userAccounts")
    List<UserAccount> getAllUsers();

    // Get data by id
    @Query("SELECT * FROM userAccounts WHERE id = :id")
    UserAccount getUserById(String id);

    @Query("SELECT * FROM userAccounts WHERE username = :username")
    UserAccount getUserByUsername(String username);
}
