package com.sbutterfly.differential;

/**
 * Created by Sergei on 03.11.14.
 */
public interface ODEMethod {
    Vector next(Function function, Vector x, double h);

    int getP();
}
