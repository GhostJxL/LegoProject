package com.legoproject.lego;

/**
 * Created by dell on 2019/9/3.
 */

public class Lego {
    private int legoColor, legoWidth, legoLength, legoNumber;

    public Lego(int legoColor, int legoWidth, int legoLength, int legoNumber){
        this.legoColor = legoColor;
        this.legoWidth = legoWidth;
        this.legoLength = legoLength;
        this.legoNumber = legoNumber;
    }

    public int getLegoColor() {
        return legoColor;
    }

    public int getLegoWidth() {
        return legoWidth;
    }

    public int getLegoLength(){
        return legoLength;
    }

    public int getLegoNumber(){
        return legoNumber;
    }

}
