package com.sbutterfly.differential;

/**
 * Created by Sergei on 03.11.14.
 */
public class EulerODEMethod implements ODEMethod {

    @Override
    public Vector next(Function function, Vector x, double h) {
        Vector diffx = function.diff(x);

        double[] newValues = diffx.toArray();
        for (int i = 0; i < newValues.length; i++) {
            newValues[i] = h * newValues[i] + x.get(i);
        }

        return new Vector(newValues);
    }

    @Override
    public int getP() {
        return 1;
    }
}
