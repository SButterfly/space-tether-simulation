package com.sbutterfly.differential;

/**
 * Created by Sergei on 03.11.14.
 */
public class RungeKuttaODEMethod implements ODEMethod {

    private Function _function;

    @Override
    public synchronized Vector Next(Function function, Vector x, double h) {

        _function = function;

        Vector k1 = K1(x, h);
        Vector k2 = Vector.mul(2, K2(x, h));
        Vector k3 = Vector.mul(2, K3(x, h));
        Vector k4 = K4(x, h);

        Vector temp = Vector.sum(k1,k2,k3,k4);
        temp = Vector.mul(h/6, temp);
        temp = Vector.sum(x, temp);

        _function = null;

        return temp;
    }

    private Vector K1(Vector x, double h){
        return _function.Diff(x);
    }

    private Vector K2(Vector x, double h){
        Vector k1 = K1(x, h);
        Vector temp = Vector.mul(h/2, k1);
        Vector result = Vector.sum(x, temp);
        return _function.Diff(result);
    }

    private Vector K3(Vector x, double h){
        Vector k1 = K2(x, h);
        Vector temp = Vector.mul(h/2, k1);
        Vector result = Vector.sum(x, temp);
        return _function.Diff(result);
    }

    private Vector K4(Vector x, double h){
        Vector k1 = K3(x, h);
        Vector temp = Vector.mul(h, k1);
        Vector result = Vector.sum(x, temp);
        return _function.Diff(result);
    }
}
