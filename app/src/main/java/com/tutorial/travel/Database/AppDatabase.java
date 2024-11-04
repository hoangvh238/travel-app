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
                            .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "travel_database")
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

                if(!categoryDao.getAllCategories().isEmpty()) {
                    PopularDAO popularDAO = instance.popularDAO();
                    popularDAO.insert(new PopularDomain(
                            "Miami Beach",
                            "United States",
                            "Mar Caible Avenida Lago",
                            "Miami Beach is one of the most famous beaches in the world, known for its stunning white sands, vibrant nightlife, and rich cultural heritage. Visitors can enjoy endless entertainment options, exquisite dining, and beautiful art deco architecture.",
                            "pic1",
                            1
                    ));
                    popularDAO.insert(new PopularDomain(
                            "Hawaii Beach",
                            "United States",
                            "Passo Rolle, TN",
                            "Hawaii Beach offers a breathtaking tropical paradise with its crystal-clear waters, lush greenery, and diverse marine life. A perfect destination for adventure seekers and relaxation enthusiasts alike, visitors can enjoy snorkeling, surfing, and scenic hiking trails.",
                            "pic2",
                            3
                    ));
                    popularDAO.insert(new PopularDomain(
                            "Bondi Beach",
                            "Australia",
                            "Bondi, Sydney",
                            "Bondi Beach is a world-renowned destination known for its golden sands, excellent surfing conditions, and vibrant community atmosphere. The beach is a hub for outdoor activities, featuring coastal walks, surfing lessons, and beachside cafes.",
                            "pic3",
                            2
                    ));

                    UserAccountDAO userDao = instance.userDAO();
                    userDao.insert(new UserAccount("admin", "Admin@123", "admin", "", "ADMIN"));
                    userDao.insert(new UserAccount("traveluser", "User@123", "traveluser", "", "USER"));
                }
            });
        }
    };
}
