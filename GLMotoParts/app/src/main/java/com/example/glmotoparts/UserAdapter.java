package com.example.glmotoparts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.glmotoparts.User;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    List<User> mlist;
    Context context;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

    public UserAdapter(List<User> mlist, Context context){
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {

        final User item = mlist.get(position);
        holder.tvUsername.setText("Username : " + item.getUsername());
        holder.tvEmail.setText("Email : " + item.getEmail());
        holder.tvPassword.setText("Password : " + item.getPassword());

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.child(item.getUsername()).setValue(null);

            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = item.getUsername();
                String Email = item.getEmail();
                String password = item.getPassword();

                Intent edit = new Intent(context, EditDataUser.class);
                edit.putExtra("username", username);
                edit.putExtra("noHp", Email);
                edit.putExtra("password", password);
                context.startActivity(edit);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername, tvEmail, tvPassword;
        private Button btnHapus, btnEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            btnHapus = itemView.findViewById(R.id.btnHapus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
