package com.sbutterfly.differential;


/**
 * Created by Sergei on 03.11.14.
 */
public interface Function {
    Vector diff(Vector x);
    int getDimension();
}
