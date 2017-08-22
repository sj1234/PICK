package com.example.sjeong.pick;

import android.content.Context;
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

public class TopAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Top10> arraylist;
    private View.OnClickListener onClickListener;

    public TopAdapter(Context context, int layout, ArrayList<Top10> arraylist, View.OnClickListener onClickListener){
        this.context = context;
        this.layout = layout;
        this.arraylist = arraylist;
        this.onClickListener = onClickListener;
    }

    //리스트 객체 내의 item의 갯수를 반환해주는 함수. 리스트 객체의 size를 반환해주면된다
    @Override
    public int getCount() {
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

    // ListView의 항목들을 출력하는 함수 position : 해당되는 항목의 Adapter에서의 위치값  convertView : 재사용할 항목의 View   arent : 항목의 View들을 포함하고 있는 ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_topadapter, parent, false);
        }

        TextView rank = (TextView) convertView.findViewById(R.id.top_rank);
        TextView name = (TextView) convertView.findViewById(R.id.top_name);

        ImageView updown = (ImageView)convertView.findViewById(R.id.updown);
        TextView compare = (TextView) convertView.findViewById(R.id.top_compare);

        rank.setText(String.valueOf(arraylist.get(position).getTop_thisrank()));
        name.setText(arraylist.get(position).getTop_name().toString());


        if(arraylist.get(position).getTop_prerank() == -1) {
            updown.setImageResource(R.drawable.new_rank);
            compare.setText("!");
        }
        else{
            int compare_int = arraylist.get(position).getTop_thisrank() -  arraylist.get(position).getTop_prerank();

            if(compare_int>0)
                updown.setImageResource(R.drawable.down);
            else if(compare_int<0){
                updown.setImageResource(R.drawable.up);
                compare_int *= -1;
            }
            else
                updown.setImageResource(R.drawable.same);
            compare.setText(String.valueOf(compare_int));
        }

        if(onClickListener != null) {
            name.setTag(arraylist.get(position).getTop_name().toString());
            name.setOnClickListener(onClickListener);
        }

        return convertView;
    }
}
