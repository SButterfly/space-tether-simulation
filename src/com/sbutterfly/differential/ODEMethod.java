package com.sbutterfly.differential;

/**
 * Created by Sergei on 03.11.14.
 */
public interface ODEMethod {
    Vector Next(Function function, Vector x, double h);
}
