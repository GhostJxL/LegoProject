package com.legoproject.lego;

/**
 * Created by dell on 2019/9/3.
 */

public class Lego {
    private int legoColor, legoWidth, legoLength, legoNumber;
    private boolean isDamaged;
    public Lego(int legoColor, int legoWidth, int legoLength, int legoNumber, boolean isDamaged) {
        this.legoColor = legoColor;
        this.legoWidth = legoWidth;
        this.legoLength = legoLength;
        this.legoNumber = legoNumber;
        this.isDamaged = isDamaged;
    }

    public int getLegoColor() {
        return legoColor;
    }

    public int getLegoWidth() {
        return legoWidth;
    }

    public int getLegoLength() {
        return legoLength;
    }

    public int getLegoNumber() {
        return legoNumber;
    }

    public boolean getIsDamaged() { return isDamaged; }
}
