package com.tutorial.travel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tutorial.travel.DAOs.PopularCategoryDAO;
import com.tutorial.travel.DAOs.UserAccountDAO;
import com.tutorial.travel.Database.AppDatabase;
import com.tutorial.travel.Domain.CategoryDomain;
import com.tutorial.travel.Domain.UserAccount;
import com.tutorial.travel.R;

import java.util.List;

public class LogInActivity extends AppCompatActivity {
    private UserAccountDAO dao;
    public Button submitButton;
    public EditText userText;
    public EditText passText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dao = AppDatabase.getInstance(this).userDAO();
        submitButton = findViewById(R.id.btnSubmit);
        userText = findViewById(R.id.editUser);
        passText = findViewById(R.id.editPassword);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin(view);
            }
        });
    }

    private void onLogin(View view) {
        String username = userText.getText().toString();
        String password = passText.getText().toString();

        UserAccount currentUser = dao.getUserByUsername(username);

        if (currentUser != null && username.equals(currentUser.getUsername()) && password.equals(currentUser.getPassword())) {
            if(currentUser.getRole().equalsIgnoreCase("ADMIN")){
                Toast.makeText(getApplicationContext(), "Admin login successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "User login successfully", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
        }
    }
}
