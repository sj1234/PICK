package com.example.sjeong.pick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemDetailAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ItemDetail> arraylist;

    public ItemDetailAdapter(Context context, ArrayList<ItemDetail> arraylist){
        this.context = context;
        this.arraylist = arraylist;
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
            convertView = inflater.inflate(R.layout.list_itemdetailadapter, parent, false);
        }

        String title = arraylist.get(position).getTitle().toString();
        String detail = arraylist.get(position).getDetail().toString();

        TextView title_view = (TextView)convertView.findViewById(R.id.item_title);
        TextView detail_view = (TextView)convertView.findViewById(R.id.item_detail);
        title_view.setText(title);
        detail_view.setText(detail);

        return convertView;
    }
}
