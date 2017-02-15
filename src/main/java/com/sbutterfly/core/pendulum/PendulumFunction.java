package com.sbutterfly.core.pendulum;

import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.Vector;

/**
 * Created by Sergei on 01.01.2015.
 * Функция математического маятника
 */
@Deprecated
public class PendulumFunction extends Function {

    private double om;
    private double minusOm2;

    public PendulumFunction(double om) {
        setOm(om);
    }

    public double getOm() {
        return om;
    }

    public void setOm(double om) {
        this.om = om;
        this.minusOm2 = -om*om;
    }

    @Override
    public Vector Diff(Vector x) {

        if (x.size() != 2){
            throw new RuntimeException("Размер вектора должен быть равен 2.");
        }

        return new Vector(x.get(1), minusOm2*x.get(0));
    }
}
