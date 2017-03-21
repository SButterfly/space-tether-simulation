package com.sbutterfly.differential;

/**
 * Created by Sergei on 03.11.14.
 */
public class RungeKuttaODEMethod implements ODEMethod {

    @Override
    public Vector next(Function function, Vector x, final double h) {
        final double h6 = h / 6d;
        final double h3 = h / 3d;

        Vector k1 = k1(function, x, h);
        Vector k2 = k2(function, x, h, k1);
        Vector k3 = k3(function, x, h, k2);
        Vector k4 = k4(function, x, h, k3);

        double[] result = x.toArray();
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i] + h6 * (k1.get(i) + k4.get(i)) + h3 * (k2.get(i) + k3.get(i));
        }

        return new Vector(result);
    }

    private Vector k1(Function function, Vector x, double h) {
        return function.diff(x);
    }

    private Vector k2(Function function, Vector x, double h, Vector k1) {
        double k = h / 2d;
        double[] kk = k1.toArray();
        for (int i = 0; i < kk.length; i++) {
            kk[i] = k1.get(i) * k + x.get(i);
        }
        return function.diff(new Vector(kk));
    }

    private Vector k3(Function function, Vector x, double h, Vector k2) {
        double k = h / 2d;
        double[] kk = k2.toArray();
        for (int i = 0; i < kk.length; i++) {
            kk[i] = k2.get(i) * k + x.get(i);
        }
        return function.diff(new Vector(kk));
    }

    private Vector k4(Function function, Vector x, double h, Vector k3) {
        double k = h;
        double[] kk = k3.toArray();
        for (int i = 0; i < kk.length; i++) {
            kk[i] = k3.get(i) * k + x.get(i);
        }
        return function.diff(new Vector(kk));
    }

    public int getP() {
        return 4;
    }
}
