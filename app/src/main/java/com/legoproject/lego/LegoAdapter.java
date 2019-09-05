package com.legoproject.lego;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 2019/9/3.
 */

public class LegoAdapter extends ArrayAdapter<Lego> {
    private int resourseID;
    private String[] color = {"red", "orange", "yellow", "green", "blue-green", "blue", "purple"};

    public LegoAdapter(Context context, int textViewResourseId, List<Lego> objects){
        super(context, textViewResourseId, objects);
        resourseID = textViewResourseId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Lego lego = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourseID, parent, false);
        TextView legoColor = (TextView)view.findViewById(R.id.lego_color);
        TextView legoWidth = (TextView)view.findViewById(R.id.lego_width);
        TextView legoLength = (TextView)view.findViewById(R.id.lego_length);
        TextView legoNumber = (TextView)view.findViewById(R.id.lego_number);
        legoColor.setText(color[lego.getLegoColor()]);
        legoWidth.setText(String.valueOf(lego.getLegoWidth()));
        legoLength.setText(String.valueOf(lego.getLegoLength()));
        legoNumber.setText(String.valueOf(lego.getLegoNumber()));
        return view;
    }

}
