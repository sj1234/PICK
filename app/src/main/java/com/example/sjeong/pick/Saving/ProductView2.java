package com.example.sjeong.pick.Saving;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.sjeong.pick.R;

/**
 * Created by mijin on 2017-08-11.
 */

public class ProductView2 extends LinearLayout {
    public ProductView2(Context context) {
        super(context);

        init(context);
    }

    public ProductView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_product2, this, true);
    }
}
