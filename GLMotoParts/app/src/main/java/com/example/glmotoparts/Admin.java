package com.example.glmotoparts;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    private ImageView ivKeluar;
    private RecyclerView rvUser;

    private Button btnBuatTagihan;

    private DatabaseReference database;

    private UserAdapter adapter;

    private ArrayList<User> arrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ivKeluar = findViewById(R.id.ivKeluar);
        rvUser = findViewById(R.id.rvUser);
        btnBuatTagihan = findViewById(R.id.btnBuatTagihan);


        ivKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        database = FirebaseDatabase.getInstance().getReference("users");

        rvUser = findViewById(R.id.rvUser);
        rvUser.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUser.setLayoutManager(layoutManager);
        rvUser.setItemAnimator(new DefaultItemAnimator());

        tampilData();
    }

    private void tampilData() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList = new ArrayList<>();

                for (DataSnapshot item : snapshot.getChildren()){

                    User user = new User(item.child("username").getValue(String.class),
                            item.child("email").getValue(String.class),
                            item.child("password").getValue(String.class));
                    arrayList.add(user);

                }

                adapter = new UserAdapter(arrayList, Admin.this);
                rvUser.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
