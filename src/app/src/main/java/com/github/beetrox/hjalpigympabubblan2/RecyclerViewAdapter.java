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

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Drill> drills;


    public RecyclerViewAdapter(Context context, List<Drill> drills) {
        this.context = context;
        this.drills = drills;
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

        holder.drillName.setText(drills.get(position).getName());
//        holder.drillThumbnail.setImageResource(drills.get(position).getImage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DrillsActivity.class);

                intent.putExtra("drillName", drills.get(position).getName());
                intent.putExtra("drillDescription", drills.get(position).getDescription());
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
