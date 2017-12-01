package com.example.sjeong.pick;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mijin on 2017-12-01.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Hot> Hots;

    public MyAdapter(ArrayList<Hot> hots) {
        Hots = hots;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_hot,parent,false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Name.setText(Hots.get(position).getName());
        holder.maxRate.setText(Hots.get(position).getMaxRate().toString());
        holder.bankIcon.setImageResource(Hots.get(position).getBankIcon());
    }

    @Override
    public int getItemCount() {
        return Hots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Name, maxRate;
        public ImageView bankIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.Name);
            maxRate = (TextView) itemView.findViewById(R.id.maxRate);
            bankIcon = (ImageView) itemView.findViewById(R.id.bankIcon);
        }
    }
}
