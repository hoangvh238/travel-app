package com.tutorial.travel.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tutorial.travel.DAOs.PopularCategoryDAO;
import com.tutorial.travel.DAOs.PopularDAO;
import com.tutorial.travel.DAOs.UserAccountDAO;
import com.tutorial.travel.Domain.CategoryDomain;
import com.tutorial.travel.Domain.PopularDomain;
import com.tutorial.travel.Domain.UserAccount;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;

@Database(entities = {UserAccount.class, PopularDomain.class, CategoryDomain.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract PopularDAO popularDAO();
    public abstract PopularCategoryDAO categoryDAO();
    public abstract UserAccountDAO userDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room
                            .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "travel_db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    private final static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Populate the database in the background.
            databaseWriteExecutor.execute(() -> {
                PopularCategoryDAO categoryDao = instance.categoryDAO();
                categoryDao.insertCategory(new CategoryDomain("Historical Sites", "cat1"));
                categoryDao.insertCategory(new CategoryDomain("Architectural Marvels", "cat2"));
                categoryDao.insertCategory(new CategoryDomain("Ancient Civilizations", "cat3"));
                categoryDao.insertCategory(new CategoryDomain("Natural Wonders", "cat4"));
                categoryDao.insertCategory(new CategoryDomain("Cultural Heritage", "cat5"));

                UserAccountDAO userDao = instance.userDAO();
                userDao.insert(new UserAccount("admin", "Admin@123", "admin", "", "ADMIN"));
                userDao.insert(new UserAccount("traveluser", "User@123", "traveluser", "", "USER"));
            });
        }
    };
}
