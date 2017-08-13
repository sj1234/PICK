package com.example.sjeong.pick;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mijin on 2017-08-09.
 */

public class ProductFragment extends Fragment {

    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_product, container, false);

        context = getActivity().getApplicationContext();
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        ProductAdapter adapter = new ProductAdapter();
        adapter.addItem(new Item2("IBK든든","0.95","혜택빵빵"));
        adapter.addItem(new Item2("국민든든","0.85","대박상품"));

        listView.setAdapter(adapter);

        return rootView;
    }

    class ProductAdapter extends BaseAdapter {
        ArrayList<Item2> items = new ArrayList<Item2>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ProductView pView = new ProductView(context);
            Item2 item = items.get(i);
            ((TextView)pView.findViewById(R.id.title)).setText(item.getTitle());
            ((TextView)pView.findViewById(R.id.profit)).setText(item.getProfit());
            ((TextView)pView.findViewById(R.id.profit2)).setText(item.getProfit());
            ((TextView)pView.findViewById(R.id.content)).setText(item.getContent());
            return pView;
        }


        public void addItem(Item2 item){
            items.add(item);
        }
    }
}
