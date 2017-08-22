package com.example.sjeong.pick;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemSearchFragment extends Fragment {

    public ItemSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_search,null);

        String text = getArguments().getString("searchtext"); // 전달한 key 값

        ListView listView = (ListView)view.findViewById(R.id.listitem);
        ArrayList<Item> items = new ArrayList<Item>();

        items = itemSample();
        for(int i = 0; i<items.size(); i++){
            Log.i("Test", "Fragment items : "+ items.get(i).getItem_name());
        }

        ItemAdapter listAdapter = new ItemAdapter(getActivity(), R.layout.list_itemadapter, items, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemname = v.getTag().toString();

                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra("name",itemname);
                startActivity(intent);
            }
        }, 20000);

        listView.setAdapter(listAdapter);
        return view;
    }

    public ArrayList<Item> itemSample(){

        ArrayList<Item> items = new ArrayList<Item>();

        Item item = new Item();
        item.setItem_name("i-ONE 놀이터 예금");
        item.setItem_bank("IBK 기업은행");
        item.setItem_persent((float) 1.6);
        items.add(item);

        Item item1 = new Item();
        item1.setItem_name("i-ONE 1,000 예금");
        item1.setItem_bank("IBK 기업은행");
        item1.setItem_persent((float) 1.78);
        items.add(item1);

        Item item2 = new Item();
        item2.setItem_name("IBK 직장인적금");
        item2.setItem_bank("IBK 기업은행");
        item2.setItem_persent((float) 1.95);
        items.add(item2);

        Item item3 = new Item();
        item3.setItem_name("i-ONE 놀이터 적금");
        item3.setItem_bank("IBK 기업은행");
        item3.setItem_persent((float) 2.15);
        items.add(item3);


        Log.i("Test", "items : " + items.size() +" / " + items.get(1));
        return items;
    }

}
