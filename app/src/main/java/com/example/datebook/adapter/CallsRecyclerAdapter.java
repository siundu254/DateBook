package com.example.datebook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.model.InitiateCallModel;
import com.example.datebook.model.InitiateChatModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallsRecyclerAdapter extends RecyclerView.Adapter<CallsRecyclerAdapter.ViewHolder> {

    private List<InitiateCallModel> model;
    private Context context;

    private FirebaseDatabase mCallsDb;
    private DatabaseReference mCallsRef;

    public CallsRecyclerAdapter(List<InitiateCallModel> model) { this.model = model; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_calls_item,
                parent,
                false
        );
        mCallsDb = FirebaseDatabase.getInstance();
        mCallsRef = mCallsDb.getReference();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InitiateCallModel chatModel = model.get(position);
        holder.mVideoProfileDate.setText(chatModel.date);
        mCallsRef.child("users").child("profile").child(chatModel.recipient_id)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.mVideoProfileName.setText(snapshot.child("publicName").getValue().toString());

                        Picasso.get().load(snapshot.child("publicThumbnail").getValue().toString())
                                .placeholder(R.drawable.ic_account_circle_black_24dp)
                                .into(holder.mVideoImageProfile);

                        if (snapshot.hasChild("userPresence")) {
                            if (!snapshot.child("userPresence").getValue().toString().equals("true")) {
                                holder.mImageOnline.setBackgroundColor(R.color.colorHighlight);
                            } else {
                                holder.mImageOnline.setBackgroundColor(R.color.colorForestGreen);
                            }
                        } else {
                            holder.mImageOnline.setBackgroundColor(R.color.colorHighlight);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mVideoImageProfile;
        private TextView mVideoProfileName;
        private TextView mVideoProfileDate;
        private ImageView mImageOnline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mVideoImageProfile = itemView.findViewById(R.id.imageVideoCallProfile);
            mVideoProfileName = itemView.findViewById(R.id.textVideoCallUserName);
            mVideoProfileDate = itemView.findViewById(R.id.textVideoCallDate);
            mImageOnline = itemView.findViewById(R.id.imageCallsOnline);
        }
    }
}
