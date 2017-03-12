package com.sbutterfly.differential;

/**
 * Created by Sergei on 03.11.14.
 */
public class RungeKuttaODEMethod implements ODEMethod {

    private Function function;

    @Override
    public synchronized Vector next(Function function, Vector x, final double h) {

        this.function = function;
        final double h6 = h / 6d;
        final double h3 = h / 3d;

        Vector k1 = Vector.mul(h6, k1(x, h));
        Vector k2 = Vector.mul(h3, k2(x, h));
        Vector k3 = Vector.mul(h3, k3(x, h));
        Vector k4 = Vector.mul(h6, k4(x, h));

        this.function = null;

        return Vector.sum(x, k1, k2, k3, k4);
    }

    private Vector k1(final Vector x, final double h) {
        return function.diff(x);
    }

    private Vector k2(final Vector x, final double h) {
        Vector k1 = k1(x, h);
        Vector result = Vector.mulThenSum(k1, h / 2d, x);
        return function.diff(result);
    }

    private Vector k3(final Vector x, final double h) {
        Vector k2 = k2(x, h);
        Vector result = Vector.mulThenSum(k2, h / 2d, x);
        return function.diff(result);
    }

    private Vector k4(final Vector x, final double h) {
        Vector k3 = k3(x, h);
        Vector result = Vector.mulThenSum(k3, h, x);
        return function.diff(result);
    }

    public int getP() {
        return 4;
    }
}
