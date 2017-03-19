package com.sbutterfly.differential;

/**
 * Created by Sergei on 03.11.14.
 */
public class EulerODEMethod implements ODEMethod {

    @Override
    public Vector next(Function function, Vector x, double h) {
        Vector diffx = function.diff(x);
        Vector mul = Vector.mul(h, diffx);

        return Vector.sum(x, mul);
    }

    @Override
    public int getP() {
        return 1;
    }
}
