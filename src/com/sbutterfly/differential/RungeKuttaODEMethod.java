package com.sbutterfly.differential;

/**
 * Created by Sergei on 03.11.14.
 */
public class RungeKuttaODEMethod implements ODEMethod {

    private Function _function;

    @Override
    public synchronized Vector Next(Function function, Vector x, final double h) {

        _function = function;
        final double h6 = h / 6d;
        final double h3 = h / 3d;

        Vector k1 = Vector.mul(h6, K1(x, h));
        Vector k2 = Vector.mul(h3, K2(x, h));
        Vector k3 = Vector.mul(h3, K3(x, h));
        Vector k4 = Vector.mul(h6, K4(x, h));

        _function = null;

        return Vector.sum(x, k1, k2, k3, k4);
    }

    private Vector K1(final Vector x, final double h) {
        return _function.Diff(x);
    }

    private Vector K2(final Vector x, final double h) {
        Vector k1 = K1(x, h);
        Vector result = Vector.mulThenSum(k1, h / 2d, x);
        return _function.Diff(result);
    }

    private Vector K3(final Vector x, final double h) {
        Vector k2 = K2(x, h);
        Vector result = Vector.mulThenSum(k2, h / 2d, x);
        return _function.Diff(result);
    }

    private Vector K4(final Vector x, final double h) {
        Vector k3 = K3(x, h);
        Vector result = Vector.mulThenSum(k3, h, x);
        return _function.Diff(result);
    }
}
