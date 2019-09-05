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
        TextView legoColor = (TextView) view.findViewById(R.id.lego_color);
        TextView legoSize = (TextView) view.findViewById(R.id.lego_size);
        TextView legoNumber = (TextView) view.findViewById(R.id.lego_number);
        if (!lego.getIsDamaged()) {
            legoColor.setText(color[lego.getLegoColor()]);
            legoSize.setText(String.valueOf(lego.getLegoWidth()) + " X " + String.valueOf(lego.getLegoLength()) + " Lego Board");
            legoNumber.setText("X" + String.valueOf(lego.getLegoNumber()));
        } else {
            legoColor.setText("");
            if (lego.getLegoNumber() == 1){
                legoSize.setText("Damaged Board");
                legoNumber.setText("X" + String.valueOf(lego.getLegoNumber()));}
            else{
                legoSize.setText("Damaged Boards");
                legoNumber.setText("X" + String.valueOf(lego.getLegoNumber()));}
        }
        return view;
    }

}
