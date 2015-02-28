package com.sbutterfly.core.pendulum;

import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.differential.Function;

/**
 * Created by Sergei on 12.02.2015.
 */
public class PendulumModel extends ODEBaseModel {

    public PendulumModel() {
        setInitialParameter(0, 3);
        setStartParameter(0, 5);
    }

    @Override
    public int getNumberOfIterations() {
        return (int) (getODETime()/0.01);
    }

    @Override
    public Function getFunction() {
        double om = getInitialParameter(0);
        return new PendulumFunction(om);
    }

    @Override
    public String[] paramsNames() {
        return new String[] {"y", "v"};
    }

    @Override
    public String[] initialParamsNames() {
        return new String[] {"om"};
    }
}
