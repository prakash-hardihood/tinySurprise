package com.kratav.tinySurprise.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.utils.GlobalApp;

import java.util.List;

public class ImageAdapter1 extends BaseAdapter {
    private Context MyContext;
    private Integer[] mThumbIds;
    private String[] text;
    private List<Integer> excludeOptions;

    public ImageAdapter1(Context _MyContext, Integer[] images, String[] txt, List<Integer> optionToHide) {
        MyContext = _MyContext;
        mThumbIds = images;
        text = txt;
        excludeOptions = optionToHide;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder1 holder1;

        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            convertView = LayoutInflater.from(MyContext).inflate(
                    R.layout.int_grid_item, null);

            holder1 = new ViewHolder1();
            holder1.imgIcon = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            holder1.lblIcom = (TextView) convertView
                    .findViewById(R.id.textView1);
            holder1.layout = (LinearLayout) convertView
                    .findViewById(R.id.layout);
            convertView.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) convertView.getTag();
        }

        holder1.imgIcon.setImageResource(mThumbIds[position]);
        holder1.lblIcom.setText(text[position]);

        if (position % 2 == 0)
            holder1.layout.setBackgroundColor(0x30F1E7DD);
        else
            holder1.layout.setBackgroundColor(Color.WHITE);

        if (excludeOptions != null && excludeOptions.contains(position)) {
           // holder1.imgIcon.setImageResource(GlobalApp.occasionDisable[position]);
        }else{
            holder1.imgIcon.setImageResource(mThumbIds[position]);
        }
        return convertView;
    }

    class ViewHolder1 {
        private ImageView imgIcon;
        private TextView lblIcom;
        private LinearLayout layout;

    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

}
