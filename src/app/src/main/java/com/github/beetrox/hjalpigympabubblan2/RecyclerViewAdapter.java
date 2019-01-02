package com.github.beetrox.hjalpigympabubblan2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Drill> drills;
    private StorageReference storageReference;


    public RecyclerViewAdapter(Context context, List<Drill> drills) {
        this.context = context;
        this.drills = drills;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.drill_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Drill drill = drills.get(position);

        Context imageContext = holder.drillThumbnail.getContext();
        holder.drillName.setText(drill.getName());
        Glide.with(imageContext).load(drill.getImageUrl()).into(holder.drillThumbnail);
//        holder.drillThumbnail.setImageResource(drills.get(position).getImage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DrillsActivity.class);

                // send generated unique id
                intent.putExtra("drillId", drill.getDrillId());
                intent.putExtra("drillName", drill.getName());
                intent.putExtra("drillDescription", drill.getDescription());
                intent.putExtra("drillCategory", drill.getCategory());
                intent.putExtra("imageUrl", drill.getImageUrl());
                intent.putExtra("drillUserId", drill.getUserId());
//                intent.putExtra("Thumbnail",drills.get(position).getImage());
                // start the activity
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return drills.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView drillName;
        ImageView drillThumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            drillName = (TextView) itemView.findViewById(R.id.drillNameTextView) ;
            drillThumbnail = (ImageView) itemView.findViewById(R.id.drillImage);
            cardView = (CardView) itemView.findViewById(R.id.drillCardView);
        }
    }
}