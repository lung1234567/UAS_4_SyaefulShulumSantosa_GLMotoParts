package com.example.glmotoparts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HomeAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_add_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new HomeAdd())
                    .commit();
        }
    }
}