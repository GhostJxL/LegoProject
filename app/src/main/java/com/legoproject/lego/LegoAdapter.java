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
    private String[] color = {"white", "orange", "yellow", "green", "blue-green", "blue"};

    public LegoAdapter(Context context, int textViewResourseId, List<Lego> objects) {
        super(context, textViewResourseId, objects);
        resourseID = textViewResourseId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lego lego = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourseID, parent, false);
        if (!lego.getIsDamaged()) {
            TextView legoColor = (TextView) view.findViewById(R.id.lego_color);
            TextView legoSize = (TextView) view.findViewById(R.id.lego_size);
            TextView legoNumber = (TextView) view.findViewById(R.id.lego_number);
            legoColor.setText(color[lego.getLegoColor()]);
            legoSize.setText(String.valueOf(lego.getLegoWidth()) + " X " + String.valueOf(lego.getLegoLength()) + " Lego Board");
            legoNumber.setText(" X" + String.valueOf(lego.getLegoNumber()));
        } else {
            TextView legoDamaged = (TextView) view.findViewById(R.id.lego_size);
            if (lego.getLegoNumber() == 1)
                legoDamaged.setText(String.valueOf(lego.getLegoNumber()) + " Damaged Board");
            else
                legoDamaged.setText(String.valueOf(lego.getLegoNumber()) + " Damaged Boards");
        }
        return view;
    }

}
