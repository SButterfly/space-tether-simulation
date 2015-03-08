package com.sbutterfly.core.rope;

import com.sbutterfly.core.Customable;
import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.TimeVector;

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
    public String[] paramsExtNames() {
        return new String[]{"м", "м/с", "рад", "рад/с", "рад", "рад/с"};
    }

    @Override
    public String[] initialParamsNames() {
        return new String[]{"m1, кг", "m2, кг", "p, кг/м3", "a", "b", "Lk, м"};
    }

    @Override
    public String[] customParamsNames() {
        return new String[]{"t", "x", "y"};
    }

    @Override
    public String[] customParamsExtNames() {
        return new String[]{"с", "м", "м"};
    }

    @Override
    public Customable getCustomable(int index) {

        final Customable timeCustomable = new Customable() {
            @Override
            public double customize(TimeVector vector) {
                return vector.getTime();
            }
        };

        final Customable xCustomable = new Customable() {
            @Override
            public double customize(TimeVector vector) {
                return Math.sin(vector.get(2)) * vector.get(0);
            }
        };

        final Customable yCustomable = new Customable() {
            @Override
            public double customize(TimeVector vector) {
                return Math.cos(vector.get(2)) * vector.get(0);
            }
        };

        if (index == 0) {
            return timeCustomable;
        }

        if (index == 1) {
            return xCustomable;
        }

        if (index == 2) {
            return yCustomable;
        }

        throw new NumberFormatException("index is out of range");
    }
}
