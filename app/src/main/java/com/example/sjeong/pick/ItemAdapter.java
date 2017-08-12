package com.example.sjeong.pick;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    public ItemAdapter(Context context, int layout, ArrayList<Item> arraylist, View.OnClickListener onClickListener){
        this.context = context;
        this.layout = layout;
        this.arraylist = arraylist;
        this.onClickListener = onClickListener;
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
        TextView detail = (TextView) convertView.findViewById(R.id.item_detail);

        name.setText(arraylist.get(position).getItem_name().toString());
        detail.setText(arraylist.get(position).getItem_detail().toString());

        if(onClickListener != null) {
            name.setTag(arraylist.get(position).getItem_name().toString());
            name.setOnClickListener(onClickListener);
        }

        return convertView;
    }
}
