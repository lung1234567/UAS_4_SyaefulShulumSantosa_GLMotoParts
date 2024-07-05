package com.example.glmotoparts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
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

public class HomeAdapter extends FirebaseRecyclerAdapter<HomeModel, HomeAdapter.myViewHolder> {
    private FirebaseRecyclerOptions<HomeModel> adapteroptions;
    private SearchView searchView;
    private Query query;
    private Fragment fragment;

    public HomeAdapter(@NonNull FirebaseRecyclerOptions<HomeModel> options, SearchView searchView, Fragment fragment) {
        super(options);
        this.adapteroptions = options;
        this.searchView = searchView;
        this.fragment = fragment;
        this.query = FirebaseDatabase.getInstance().getReference().child("sparepart");
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull HomeModel model) {
        holder.nama.setText(model.getNama());
        holder.harga.setText(model.getHarga());
        holder.deskripsi.setText(model.getDeskripsi());

        Glide.with(holder.itemView)
                .load(model.getGambarurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the ReviewDialogFragment
                ReviewDialogFragment reviewDialogFragment = ReviewDialogFragment.newInstance(
                        model.getNama(),
                        model.getHarga(),
                        model.getDeskripsi(),
                        model.getGambarurl()
                );
                reviewDialogFragment.show(((HomeFragment) fragment).getFragmentManager(), "ReviewDialogFragment");
            }
        });
        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1200).create();

                View view = dialogPlus.getHolderView();
                EditText nama = view.findViewById(R.id.txtnama);
                EditText harga = view.findViewById(R.id.txtharga);
                EditText gambarUrl = view.findViewById(R.id.txtgambarurl);
                EditText deskripsi = view.findViewById(R.id.txtdeskripsi);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                nama.setText(model.getNama());
                harga.setText(model.getHarga());
                gambarUrl.setText(model.getGambarurl());
                deskripsi.setText(model.getDeskripsi());
                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("nama", nama.getText().toString());
                        map.put("harga", harga.getText().toString());
                        map.put("gambarurl", gambarUrl.getText().toString());
                        map.put("deskripsi", deskripsi.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("sparepart")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.nama.getContext(), "Data Berhasil update.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.nama.getContext(), "Errooooooor update.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.nama.getContext());
                builder.setTitle("Apakah yakin ingin dihapus?");
                builder.setMessage("Gabisa di undo loh..");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("sparepart")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.nama.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView nama;
        TextView harga;
        TextView deskripsi;
        ImageButton btnedit, btndelete;
        CardView cardView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            nama = (TextView) itemView.findViewById(R.id.nama);
            harga = (TextView) itemView.findViewById(R.id.harga);
            deskripsi = (TextView) itemView.findViewById(R.id.deskripsi);
            btnedit = (ImageButton)itemView.findViewById(R.id.btnedit);
            btndelete = (ImageButton) itemView.findViewById(R.id.btndelete);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public void updateOptions(FirebaseRecyclerOptions<HomeModel> newoptions) {
        this.adapteroptions = newoptions;
        stopListening();
        Log.d("HomeAdapter", "Updating options: " + newoptions);
        notifyDataSetChanged();
        startListening();
    }


}