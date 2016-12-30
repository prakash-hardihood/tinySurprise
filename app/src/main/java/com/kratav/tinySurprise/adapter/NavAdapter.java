package com.kratav.tinySurprise.adapter;

/**
 * Created by DELL on 6/22/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.activities.HomeActivity;
import com.kratav.tinySurprise.activities.MainActivity;
import com.kratav.tinySurprise.activities.UserCartActivity;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.utils.GlobalApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NavAdapter extends BaseExpandableListAdapter {

    public LayoutInflater minflater;
    public Activity activity;
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    public int number;

    public String txt1 = null, txt2 = null, txt3= null, txt4= null, txt5= null, txt6= null, txt7= null, txt8= null, txt9= null, txt10= null, txt11= null, txt12= null;


    public NavAdapter(Context context, List<String> listDataHeader,
                      HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if(groupPosition==0){
            txt1 = (String) getChild(groupPosition, 0);
            txt2 = (String) getChild(groupPosition, 1);
            txt3 = (String) getChild(groupPosition, 2);
            txt4 = (String) getChild(groupPosition, 3);
            txt5 = (String) getChild(groupPosition, 4);
            txt6 = (String) getChild(groupPosition, 5);
            txt7 = (String) getChild(groupPosition, 6);
            txt8 = (String) getChild(groupPosition, 7);
            txt9 = (String) getChild(groupPosition, 8);
            txt10 = (String) getChild(groupPosition, 9);
            txt11 = (String) getChild(groupPosition, 10);
            txt12 = (String) getChild(groupPosition, 11);
        }
        else if(groupPosition==1){
            txt1 = (String) getChild(groupPosition, 0);
            txt2 = (String) getChild(groupPosition, 1);
            txt3 = (String) getChild(groupPosition, 2);
            txt4 = (String) getChild(groupPosition, 3);
            txt5 = (String) getChild(groupPosition, 4);
            txt6 = (String) getChild(groupPosition, 5);
            txt7 = (String) getChild(groupPosition, 6);
            txt8 = (String) getChild(groupPosition, 7);
            txt9 = (String) getChild(groupPosition, 8);
        }

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }



        if(groupPosition==0) {
            RelativeLayout layout1 = (RelativeLayout) convertView.findViewById(R.id.layout1);
            RelativeLayout layout2 = (RelativeLayout) convertView.findViewById(R.id.layout2);
            RelativeLayout layout3 = (RelativeLayout) convertView.findViewById(R.id.layout3);
            RelativeLayout layout4 = (RelativeLayout) convertView.findViewById(R.id.layout4);
            RelativeLayout layout5 = (RelativeLayout) convertView.findViewById(R.id.layout5);
            RelativeLayout layout6 = (RelativeLayout) convertView.findViewById(R.id.layout6);
            RelativeLayout layout7 = (RelativeLayout) convertView.findViewById(R.id.layout7);
            RelativeLayout layout8 = (RelativeLayout) convertView.findViewById(R.id.layout8);
            RelativeLayout layout9 = (RelativeLayout) convertView.findViewById(R.id.layout9);
            RelativeLayout layout10 = (RelativeLayout) convertView.findViewById(R.id.layout10);
            RelativeLayout layout11 = (RelativeLayout) convertView.findViewById(R.id.layout11);
            RelativeLayout layout12 = (RelativeLayout) convertView.findViewById(R.id.layout12);

            TextView text1 = (TextView) convertView.findViewById(R.id.txt1);
            TextView text2 = (TextView) convertView.findViewById(R.id.txt2);
            TextView text3 = (TextView) convertView.findViewById(R.id.txt3);
            TextView text4 = (TextView) convertView.findViewById(R.id.txt4);
            TextView text5 = (TextView) convertView.findViewById(R.id.txt5);
            TextView text6 = (TextView) convertView.findViewById(R.id.txt6);
            TextView text7 = (TextView) convertView.findViewById(R.id.txt7);
            TextView text8 = (TextView) convertView.findViewById(R.id.txt8);
            TextView text9 = (TextView) convertView.findViewById(R.id.txt9);
            TextView text10 = (TextView) convertView.findViewById(R.id.txt10);
            TextView text11 = (TextView) convertView.findViewById(R.id.txt11);
            TextView text12 = (TextView) convertView.findViewById(R.id.txt12);
            ImageView img1 = (ImageView) convertView.findViewById(R.id.img1);
            ImageView img2 = (ImageView) convertView.findViewById(R.id.img2);
            ImageView img3 = (ImageView) convertView.findViewById(R.id.img3);
            ImageView img4 = (ImageView) convertView.findViewById(R.id.img4);
            ImageView img5 = (ImageView) convertView.findViewById(R.id.img5);
            ImageView img6 = (ImageView) convertView.findViewById(R.id.img6);
            ImageView img7 = (ImageView) convertView.findViewById(R.id.img7);
            ImageView img8 = (ImageView) convertView.findViewById(R.id.img8);
            ImageView img9 = (ImageView) convertView.findViewById(R.id.img9);
            ImageView img10 = (ImageView) convertView.findViewById(R.id.img10);
            ImageView img11 = (ImageView) convertView.findViewById(R.id.img11);
            ImageView img12 = (ImageView) convertView.findViewById(R.id.img12);


            text1.setText(txt1);
            text2.setText(txt2);
            text3.setText(txt3);
            text4.setText(txt4);
            text5.setText(txt5);
            text6.setText(txt6);
            text7.setText(txt7);
            text8.setText(txt8);
            text9.setText(txt9);
            text10.setText(txt10);
            text11.setText(txt11);
            text12.setText(txt12);
            img1.setImageResource(R.drawable.bof);
            img2.setImageResource(R.drawable.gif);
            img3.setImageResource(R.drawable.hubby);
            img4.setImageResource(R.drawable.wif);
            img5.setImageResource(R.drawable.dad);
            img6.setImageResource(R.drawable.mom);
            img7.setImageResource(R.drawable.bro);
            img8.setImageResource(R.drawable.sis);
            img9.setImageResource(R.drawable.kids);
            img10.setImageResource(R.drawable.couples);
            img11.setImageResource(R.drawable.boss);
            img12.setImageResource(R.drawable.friends);






            text1.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text2.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text3.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text4.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text5.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text6.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text7.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text8.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text9.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text10.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text11.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text12.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));

            layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt1);
                    _context.startActivity(intent);
                }
            });
            layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt2);
                    _context.startActivity(intent);
                }
            });
            layout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt3);
                    _context.startActivity(intent);
                }
            });
            layout4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt4);
                    _context.startActivity(intent);
                }
            });
            layout5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt5);
                    _context.startActivity(intent);
                }
            });
            layout6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt6);
                    _context.startActivity(intent);
                }
            });
            layout7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt7);
                    _context.startActivity(intent);
                }
            });
            layout8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt8);
                    _context.startActivity(intent);
                }
            });
            layout9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt9);
                    _context.startActivity(intent);
                }
            });
            layout10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt10);
                    _context.startActivity(intent);
                }
            });
            layout11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt11);
                    _context.startActivity(intent);
                }
            });
            layout12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt12);
                    _context.startActivity(intent);
                }
            });

        }



        else if(groupPosition==1){
            RelativeLayout layout1 = (RelativeLayout) convertView.findViewById(R.id.layout1);
            RelativeLayout layout2 = (RelativeLayout) convertView.findViewById(R.id.layout2);
            RelativeLayout layout3 = (RelativeLayout) convertView.findViewById(R.id.layout3);
            RelativeLayout layout4 = (RelativeLayout) convertView.findViewById(R.id.layout4);
            RelativeLayout layout5 = (RelativeLayout) convertView.findViewById(R.id.layout5);
            RelativeLayout layout6 = (RelativeLayout) convertView.findViewById(R.id.layout6);
            RelativeLayout layout7 = (RelativeLayout) convertView.findViewById(R.id.layout7);
            RelativeLayout layout8 = (RelativeLayout) convertView.findViewById(R.id.layout8);
            RelativeLayout layout9 = (RelativeLayout) convertView.findViewById(R.id.layout9);
            TextView text1 = (TextView) convertView.findViewById(R.id.txt1);
            TextView text2 = (TextView) convertView.findViewById(R.id.txt2);
            TextView text3 = (TextView) convertView.findViewById(R.id.txt3);
            TextView text4 = (TextView) convertView.findViewById(R.id.txt4);
            TextView text5 = (TextView) convertView.findViewById(R.id.txt5);
            TextView text6 = (TextView) convertView.findViewById(R.id.txt6);
            TextView text7 = (TextView) convertView.findViewById(R.id.txt7);
            TextView text8 = (TextView) convertView.findViewById(R.id.txt8);
            TextView text9 = (TextView) convertView.findViewById(R.id.txt9);
            ImageView img1 = (ImageView) convertView.findViewById(R.id.img1);
            ImageView img2 = (ImageView) convertView.findViewById(R.id.img2);
            ImageView img3 = (ImageView) convertView.findViewById(R.id.img3);
            ImageView img4 = (ImageView) convertView.findViewById(R.id.img4);
            ImageView img5 = (ImageView) convertView.findViewById(R.id.img5);
            ImageView img6 = (ImageView) convertView.findViewById(R.id.img6);
            ImageView img7 = (ImageView) convertView.findViewById(R.id.img7);
            ImageView img8 = (ImageView) convertView.findViewById(R.id.img8);
            ImageView img9 = (ImageView) convertView.findViewById(R.id.img9);
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.disable_layout);

            text1.setText(txt1);
            text2.setText(txt2);
            text3.setText(txt3);
            text4.setText(txt4);
            text5.setText(txt5);
            text6.setText(txt6);
            text7.setText(txt7);
            text8.setText(txt8);
            text9.setText(txt9);
            img1.setImageResource(R.drawable.birthday);
            img2.setImageResource(R.drawable.anniversary);
            img3.setImageResource(R.drawable.wedding);
            img4.setImageResource(R.drawable.congratulate);
            img5.setImageResource(R.drawable.baby_shower);
            img6.setImageResource(R.drawable.house_warming);
            img7.setImageResource(R.drawable.i_love_you);
            img8.setImageResource(R.drawable.i_am_sorry);
            img9.setImageResource(R.drawable.i_miss_you);

            text1.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text2.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text3.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text4.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text5.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text6.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text7.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text8.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));
            text9.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_REG));

            layout.setVisibility(View.GONE);

            layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt1);
                    _context.startActivity(intent);
                }
            });
            layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt2);
                    _context.startActivity(intent);
                }
            });
            layout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt3);
                    _context.startActivity(intent);
                }
            });
            layout4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt4);
                    _context.startActivity(intent);
                }
            });
            layout5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt5);
                    _context.startActivity(intent);
                }
            });
            layout6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt6);
                    _context.startActivity(intent);
                }
            });
            layout7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt7);
                    _context.startActivity(intent);
                }
            });
            layout8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt8);
                    _context.startActivity(intent);
                }
            });
            layout9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,HomeActivity.class).putExtra(AppConstant.EXTRA_LOCATION, txt9);
                    _context.startActivity(intent);
                }
            });

    }


      //  txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       // return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
            return 1;

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        if (getChildrenCount(groupPosition) == 0 ) {
            convertView.setVisibility( View.INVISIBLE );

        }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(GlobalApp.getFont(this._context, GlobalApp.LATO_SEMI));
            lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
