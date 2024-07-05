package com.example.glmotoparts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

public class ReviewDialogFragment extends DialogFragment {
    private static final String NAMA_KEY = "nama";
    private static final String HARGA_KEY = "harga";
    private static final String DESKRIPSI_KEY = "deskripsi";
    private static final String GAMBARURL_KEY = "gambarurl";

    public static ReviewDialogFragment newInstance(String nama, String harga, String deskripsi, String gambarurl) {
        ReviewDialogFragment fragment = new ReviewDialogFragment();
        Bundle args = new Bundle();
        args.putString(NAMA_KEY, nama);
        args.putString(HARGA_KEY, harga);
        args.putString(DESKRIPSI_KEY, deskripsi);
        args.putString(GAMBARURL_KEY, gambarurl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_review, container, false);

        // Initialize dialog views
        TextView namaTextView = view.findViewById(R.id.nama);
        TextView hargaTextView = view.findViewById(R.id.harga);
        TextView deskripsiTextView = view.findViewById(R.id.deskripsi);
        ImageView imageView = view.findViewById(R.id.img1);

        // Get the data from the bundle
        String nama = getArguments().getString(NAMA_KEY);
        String harga = getArguments().getString(HARGA_KEY);
        String deskripsi = getArguments().getString(DESKRIPSI_KEY);
        String gambarurl = getArguments().getString(GAMBARURL_KEY);

        // Set dialog views with data
        namaTextView.setText(nama);
        hargaTextView.setText(harga);
        deskripsiTextView.setText(deskripsi);

        // Load image using Glide
        Glide.with(this)
                .load(gambarurl)
                .into(imageView);

        return view;
    }
}