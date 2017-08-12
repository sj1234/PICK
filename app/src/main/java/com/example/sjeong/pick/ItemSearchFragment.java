package com.example.sjeong.pick;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        for(int i=1; i<4; i++){
            Item item = new Item();
            item.setItem_name(text+" "+i);
            item.setItem_detail(text+" "+i);

            items.add(item);
        }

        ItemAdapter listAdapter = new ItemAdapter(getActivity(), R.layout.list_itemadapter, items, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemname = v.getTag().toString();

                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra("name",itemname);
                startActivity(intent);
            }
        });

        listView.setAdapter(listAdapter);

        return view;
    }

}
