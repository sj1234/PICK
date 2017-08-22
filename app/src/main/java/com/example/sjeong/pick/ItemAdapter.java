package com.example.sjeong.pick;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SJeong on 2017-08-11.
 */

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Item> arraylist;
    private View.OnClickListener onClickListener;
    private int deposit;

    public ItemAdapter(Context context, int layout, ArrayList<Item> arraylist, View.OnClickListener onClickListener, int deposit){
        this.context = context;
        this.layout = layout;
        this.arraylist = arraylist;
        this.onClickListener = onClickListener;
        this.deposit = deposit;
    }

    @Override
    public int getCount()  {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_itemadapter, parent, false);
        }

        Log.i("Test",""+convertView.getHeight());
        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView persent = (TextView) convertView.findViewById(R.id.item_persent);
        TextView predict = (TextView)convertView.findViewById(R.id.item_predict);

        name.setText(arraylist.get(position).getItem_name().toString());
        persent.setText(String.valueOf(arraylist.get(position).getItem_persent())+"%");
        predict.setText("약 "+String.valueOf(arraylist.get(position).getItem_persent() * deposit)+"원");

        if(onClickListener != null) {
            name.setTag(arraylist.get(position).getItem_name().toString());
            name.setOnClickListener(onClickListener);

            ImageView image = (ImageView)convertView.findViewById(R.id.image);
            image.setTag(arraylist.get(position).getItem_name().toString());
            image.setOnClickListener(onClickListener);

            persent.setTag(arraylist.get(position).getItem_name().toString());
            persent.setOnClickListener(onClickListener);

            predict.setTag(arraylist.get(position).getItem_name().toString());
            predict.setOnClickListener(onClickListener);
        }

        return convertView;
    }
}
