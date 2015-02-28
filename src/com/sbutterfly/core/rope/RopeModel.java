package com.sbutterfly.core.rope;

import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.differential.Function;

/**
 * Created by Sergei on 27.02.2015.
 */
public class RopeModel extends ODEBaseModel {

    public RopeModel() {
        setStartParameter(0, 1);
        setStartParameter(1, 2.5);

        setInitialParameter(0, 6000);
        setInitialParameter(1, 20);
        setInitialParameter(2, 0.0002);
        setInitialParameter(3, 4);
        setInitialParameter(4, 5);
        setInitialParameter(5, 30000);
    }

    @Override
    public Function getFunction() {
        final double m1 = getInitialParameter(0);
        final double m2 = getInitialParameter(1);
        final double p = getInitialParameter(2);
        final double a = getInitialParameter(3);
        final double b = getInitialParameter(4);
        final double lk = getInitialParameter(5);

        return new RopeFunction(m1, m2, p, a, b, lk);
    }

    @Override
    public String[] paramsNames() {
        return new String[]{"L", "Lt", "O", "Ot", "B", "Bt"};
    }

    @Override
    public String[] initialParamsNames() {
        return new String[]{"m1", "m2", "p", "a", "b", "Lk"};
    }
}
