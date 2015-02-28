package com.sbutterfly.core;

import com.sbutterfly.differential.TimeVector;

/**
 * Created by Sergei on 28.02.2015.
 */
public interface Customable {
    default double customize(TimeVector vector) {
        return 0;
    }
}
