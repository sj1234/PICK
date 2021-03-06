package com.example.sjeong.pick;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by SJeong on 2017-08-11.
 */

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> arraylist;
    private View.OnClickListener onClickListener;
    private HashSet<String> prime = new HashSet<String>();
    private boolean prime_boolean = false;

    public ItemAdapter(Context context, ArrayList<Item> arraylist, String prime, View.OnClickListener onClickListener){
        this.context = context;
        this.arraylist = arraylist;
        this.onClickListener = onClickListener;

        if(!prime.isEmpty()){
            prime_boolean = true;
            String[] split = prime.split(",");
            for(String splits : split){
                (this.prime).add(splits);
            }
        }
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

        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView min_rate = (TextView) convertView.findViewById(R.id.item_min_rate);
        TextView max_rate = (TextView)convertView.findViewById(R.id.item_max_rate);
        ImageView bank_image = (ImageView)convertView.findViewById(R.id.bank_image);

        if(prime_boolean){
            String code = arraylist.get(position).getCode().toString();
            if(prime.contains(code)) {
                convertView.setBackgroundResource(R.drawable.round_pink_gradient);
                name.setTextColor(Color.WHITE);
                min_rate.setTextColor(Color.WHITE);
                max_rate.setTextColor(Color.WHITE);
                Log.i("prime", code+ " + "+prime.toString());
            }
            else {
                convertView.setBackgroundResource(R.drawable.menu);
                name.setTextColor(Color.parseColor("#515151"));
                min_rate.setTextColor(Color.parseColor("#fe6186"));
                max_rate.setTextColor(Color.parseColor("#fe6186"));
            }
        }

        name.setText(arraylist.get(position).getName().toString());
        min_rate.setText(arraylist.get(position).getCont_rate().toString()+"% ~");
        max_rate.setText(arraylist.get(position).getMax_rate().toString()+"%");


        switch(arraylist.get(position).getBank().toString()){
            case "NH농협은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nh));
                break;
            case "기업은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ibk));
                break;
            case "국민은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.kb)); break;
            case "우리은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.woori)); break;
            case "KEB하나은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.keb)); break;
            case "KDB산업은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.kdb)); break;
            case "경남은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gn)); break;
            case "광주은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gj)); break;
            case "대구은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dg)); break;
            case "부산은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bs)); break;
            case "수협은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sh)); break;
            case "스탠다드차타드은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sc)); break;
            case "씨티은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.citi)); break;
            case "우체국예금":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.post)); break;
            case "전북은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.jb)); break;
            case "제주은행":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.jj)); break;
            case "케이뱅크":
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.kbank)); break;
            case "신한은행" :
                bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shinhan)); break;
        }

        if(onClickListener != null) {
            LinearLayout item_background = (LinearLayout) convertView.findViewById(R.id.item_background);
            item_background.setTag(arraylist.get(position).getCode().toString());
            item_background.setOnClickListener(onClickListener);
        }
        return convertView;
    }
}
