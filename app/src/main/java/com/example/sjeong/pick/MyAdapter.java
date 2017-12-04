package com.example.sjeong.pick;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjeong.pick.Saving.ProductDetailActivity;

import java.util.ArrayList;

/**
 * Created by mijin on 2017-12-01.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Hot> Hots;
    private Context context;
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public MyAdapter(Context context, ArrayList<Hot> hots) {
        this.context = context;
        Hots = hots;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_hot,parent,false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prod_code=0;
                for(Hot hot : Hots){
                    if(hot.getName().equals(((TextView)v.findViewById(R.id.Name)).getText().toString())){
                        prod_code = hot.getProd_code();
                    }
                }
                Log.d("머냐",prod_code+"");
                Intent intent;
                if(prod_code>=1010&&prod_code<=1695){
                    intent = new Intent(context, ItemActivity.class);
                    intent.putExtra("data",String.valueOf(prod_code));
                    context.startActivity(intent);
                }else{
                    intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("prod_code",prod_code);
                    context.startActivity(intent);
                }
            }
        });
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    public Hot getItem(int position)
    {
        return Hots.get(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Name.setText(Hots.get(position).getName());
        holder.minRate.setText(Hots.get(position).getMinRate().toString()+"% ~");
        holder.maxRate.setText("최고 "+Hots.get(position).getMaxRate().toString()+"%");
        holder.bankIcon.setImageResource(Hots.get(position).getBankIcon());
        holder.rank.setImageResource(Hots.get(position).getRank());

    }

    @Override
    public int getItemCount() {
        return Hots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Name, maxRate, minRate;
        public ImageView bankIcon, rank;
        int prod_code;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.Name);
            minRate = (TextView) itemView.findViewById(R.id.minRate);
            maxRate = (TextView) itemView.findViewById(R.id.maxRate);
            bankIcon = (ImageView) itemView.findViewById(R.id.bankIcon);
            rank = (ImageView) itemView.findViewById(R.id.rank);

        }
    }
}
