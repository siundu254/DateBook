package com.example.datebook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.ui.MatchProfileActivity;
import com.example.datebook.R;
import com.example.datebook.model.MatchModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class NewMatchesRecyclerAdapter extends RecyclerView.Adapter<NewMatchesRecyclerAdapter.ViewHolder> {
    private List<MatchModel> matchModel;
    private Context context;
    private FirebaseDatabase mMatchProfileDb;
    private DatabaseReference mMatchProfileRef;

    public NewMatchesRecyclerAdapter(List<MatchModel> matchModel) {
        this.matchModel = matchModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_match_layout,
                parent,
                false
        );
        context = mView.getContext();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchModel model = matchModel.get(position);
        holder.mTextProfile.setText(model.public_name);
        Picasso.get().load(model.thumb_profile).placeholder(R.drawable.ic_account_circle_black_24dp).into(holder.mImageProfile);

        holder.itemView.setOnClickListener(view -> {
            Intent mIntent = new Intent(context.getApplicationContext(), MatchProfileActivity.class);
            mIntent.putExtra("user_id", model.user_id);
            context.startActivity(mIntent);
            Bungee.slideLeft(context);
        });

        mMatchProfileDb = FirebaseDatabase.getInstance();
        mMatchProfileRef = mMatchProfileDb.getReference();
        mMatchProfileRef.child("users").child("profile").child(model.user_id)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("userPresence")) {
                            if (!snapshot.child("userPresence").getValue().toString().equals("true")) {
                                holder.mImageMatchOffline.setBackgroundColor(R.color.colorHighlight);
                            } else {
                                holder.mImageMatchOffline.setBackgroundColor(R.color.colorForestGreen);
                            }
                        } else {
                            holder.mImageMatchOffline.setBackgroundColor(R.color.colorHighlight);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return matchModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mImageProfile;
        TextView mTextProfile;
        ImageView mImageMatchOffline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageProfile = itemView.findViewById(R.id.imageViewMatchProfile);
            mTextProfile = itemView.findViewById(R.id.textViewMatchProfileName);
            mImageMatchOffline = itemView.findViewById(R.id.imageViewMatchOffline);
        }
    }
}
