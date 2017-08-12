package com.example.sjeong.pick;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by mijin on 2017-08-11.
 */

public class ProductView extends LinearLayout {
    public ProductView(Context context) {
        super(context);

        init(context);
    }

    public ProductView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_product, this, true);
    }
}
