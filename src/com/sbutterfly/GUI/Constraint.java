package com.sbutterfly.GUI;

import java.awt.*;

/**
 * Created by Sergei on 15.02.2015.
 */
public class Constraint {

    GridBagConstraints c = new GridBagConstraints();

    public Constraint gridX(int value){
        c.gridx = value;
        return this;
    }

    public Constraint gridY(int value){
        c.gridy = value;
        return this;
    }

    public Constraint weightX(double value){
        c.weightx = value;
        return this;
    }

    public Constraint weightY(double value){
        c.weighty = value;
        return this;
    }

    public Constraint gridHeight(int value){
        c.gridheight = value;
        return this;
    }

    public Constraint gridWidth(int value){
        c.gridwidth = value;
        return this;
    }

    public Constraint anchor(int value){
        c.anchor = value;
        return this;
    }

    public Constraint ipadY(int value){
        c.ipady = value;
        return this;
    }

    public Constraint ipadX(int value){
        c.ipadx = value;
        return this;
    }

    public Constraint fill(int value){
        c.fill = value;
        return this;
    }

    public Constraint insets(int top, int left, int bottom, int right){
        c.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public Constraint insets(int topBottom, int leftRight){
        c.insets = new Insets(topBottom, leftRight, topBottom, leftRight);
        return this;
    }

    public Constraint insets(int value){
        c.insets = new Insets(value, value, value, value);
        return this;
    }

    public Constraint insets(Insets value){
        c.insets = value;
        return this;
    }

    public GridBagConstraints get(){
        return c;
    }

    public static Constraint New(int gridX, int gridY){
        return new Constraint().gridX(gridX).gridY(gridY);
    }
    public static Constraint New(int gridX, int gridY, int gridWidth, int gridHeight){
        return new Constraint().gridX(gridX).gridY(gridY).gridWidth(gridWidth).gridHeight(gridHeight);
    }
}
