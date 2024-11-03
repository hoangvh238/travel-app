package com.tutorial.travel.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tutorial.travel.DAOs.PopularCategoryDAO;
import com.tutorial.travel.DAOs.PopularDAO;
import com.tutorial.travel.Domain.CategoryDomain;
import com.tutorial.travel.Domain.PopularDomain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PopularDomain.class, CategoryDomain.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract PopularDAO popularDAO();
    public abstract PopularCategoryDAO categoryDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room
                            .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database")
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
                categoryDao.insertCategory(new CategoryDomain("Beaches", "cat1"));
                categoryDao.insertCategory(new CategoryDomain("Camps", "cat2"));
                categoryDao.insertCategory(new CategoryDomain("Forest", "cat3"));
                categoryDao.insertCategory(new CategoryDomain("Desert", "cat4"));
                categoryDao.insertCategory(new CategoryDomain("Mountain", "cat5"));
            });
        }
    };
}
