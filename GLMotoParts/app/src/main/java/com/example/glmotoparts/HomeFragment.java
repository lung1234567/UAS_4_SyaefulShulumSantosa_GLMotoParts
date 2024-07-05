package com.example.glmotoparts;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import android.view.Gravity;
import java.util.HashMap;
import java.util.Map;
public class HomeFragment extends Fragment {
    private HomeAdapter homeAdapter;
    private ImageButton IBmenu;
    private RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rvsparepart);
        searchView = view.findViewById(R.id.search);

        FirebaseRecyclerOptions<HomeModel> adapterOptions = new FirebaseRecyclerOptions.Builder<HomeModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("sparepart"), HomeModel.class)
                .build();

        homeAdapter = new HomeAdapter(adapterOptions, searchView, this);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDataDialogFragment addDataDialogFragment = new AddDataDialogFragment();
                addDataDialogFragment.show(getFragmentManager(), "AddDataDialogFragment");
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                Log.d("SearchQuery", "Search query: " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return true;
            }
        });

        homeAdapter.startListening();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        homeAdapter.stopListening();
    }

    private void txtSearch(String str) {
        Log.d("HomeAdapter", "Filtering by query: " + str);
        FirebaseRecyclerOptions<HomeModel> newOptions = new FirebaseRecyclerOptions.Builder<HomeModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("sparepart")
                        .orderByChild("nama").startAt(str).endAt(str), HomeModel.class)
                .build();

        Log.d("HomeAdapter", "New options: " + newOptions);

        homeAdapter.updateOptions(newOptions);
        homeAdapter.startListening();
        recyclerView.setAdapter(homeAdapter);
    }

    public static class AddDataDialogFragment extends DialogFragment {
        private EditText nama, harga, gambarUrl, deskripsi;
        private Button btnAdd, btnBack;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.activity_home_add);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            nama = dialog.findViewById(R.id.txtnama);
            harga = dialog.findViewById(R.id.txtharga);
            gambarUrl = dialog.findViewById(R.id.txtgambarurl);
            deskripsi = dialog.findViewById(R.id.txtdeskripsi);
            btnAdd = dialog.findViewById(R.id.btnAdd);
            btnBack = dialog.findViewById(R.id.btnBack);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!nama.getText().toString().trim().isEmpty() && !harga.getText().toString().trim().isEmpty() && !gambarUrl.getText().toString().trim().isEmpty() && !deskripsi.getText().toString().trim().isEmpty()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("nama", nama.getText().toString());
                        map.put("harga", harga.getText().toString());
                        map.put("gambarurl", gambarUrl.getText().toString());
                        map.put("deskripsi", deskripsi.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("sparepart").push()
                                .setValue(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Data Berhasil Dimasukkan.", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(getActivity(), "Data Gagal Dimasukkan", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return dialog;
        }
    }
}