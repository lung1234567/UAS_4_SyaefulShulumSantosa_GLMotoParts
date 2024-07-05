package com.example.glmotoparts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.orhanobut.dialogplus.DialogPlus;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.SearchView;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.orhanobut.dialogplus.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;
import android.os.Bundle;

public class HomeAdd extends Fragment {
EditText nama, harga, gambarurl, deskripsi;
Button btnAdd, btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_add, container, false);
        nama = (EditText)view.findViewById(R.id.txtnama);
        harga = (EditText)view.findViewById(R.id.txtharga);
        gambarurl = (EditText)view.findViewById(R.id.txtgambarurl);
        deskripsi = (EditText)view.findViewById(R.id.txtdeskripsi);
        btnAdd = (Button)view.findViewById(R.id.btnAdd);
        btnBack = (Button)view.findViewById(R.id.btnBack);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            insertData();
            clearAll();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireFragmentManager().popBackStack();
            }
        });
        return view;
    }
    private void insertData(){
        Map<String, Object> map = new HashMap<>();
        map.put("nama" ,nama.getText().toString());
        map.put("harga" ,harga.getText().toString());
        map.put("gambarurl" , gambarurl.getText().toString());
        map.put("deskripsi" ,deskripsi.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("sparepart").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Data Berhasil Dimasukkan.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Toast.makeText(getActivity(), "Data Gagal Dimasukkan", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void clearAll(){
        nama.setText("");
        harga.setText("");
        gambarurl.setText("");
        deskripsi.setText("");
    }
}