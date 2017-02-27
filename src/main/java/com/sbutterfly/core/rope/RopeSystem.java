package com.sbutterfly.core.rope;

import com.sbutterfly.core.Customable;
import com.sbutterfly.core.BaseSystem;
import com.sbutterfly.differential.TimeVector;

/**
 * Created by Sergei on 27.02.2015.
 */
public class RopeSystem extends BaseSystem {

    public RopeSystem() {
        setStartParameter(0, 1);
        setStartParameter(1, 2.5);

        setInitialParameter(0, 60);
        setInitialParameter(1, 20);
        setInitialParameter(2, 0.0002);
        setInitialParameter(3, 4);
        setInitialParameter(4, 5);
        setInitialParameter(5, 30000);
        setInitialParameter(6, 1000);
    }

    @Override
    public RopeFunction getFunction() {
        final double m1 = getInitialParameter(0);
        final double m2 = getInitialParameter(1);
        final double p = getInitialParameter(2);
        final double a = getInitialParameter(3);
        final double b = getInitialParameter(4);
        final double lk = getInitialParameter(5);
        final double h = getInitialParameter(6);

        return new RopeFunction(m1, m2, p, a, b, lk, h);
    }

    @Override
    public String[] paramsNames() {
        return new String[]{"L", "V", "θ", "θ˙", "β", "β˙"};
    }

    @Override
    public String[] paramsExtNames() {
        return new String[]{"м", "м/с", "рад", "рад/с", "рад", "рад/с"};
    }

    @Override
    public String[] initialParamsNames() {
        return new String[]{"m1, кг", "m2, кг", "ρ, кг/м3", "a", "b", "Lk, м", "H, км"};
    }

    @Override
    public String[] customParamsNames() {
        return new String[]{"t", "x", "y", "T"};
    }

    @Override
    public String[] customParamsExtNames() {
        return new String[]{"с", "м", "м", "H"};
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
                return -Math.sin(vector.get(2)) * vector.get(0);
            }
        };

        final Customable yCustomable = new Customable() {
            @Override
            public double customize(TimeVector vector) {
                return -Math.cos(vector.get(2)) * vector.get(0);
            }
        };

        final Customable tCustomable = new Customable() {
            final RopeFunction function = getFunction();

            @Override
            public double customize(TimeVector vector) {
                return function.T(vector.get(0), vector.get(1));
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

        if (index == 3) {
            return tCustomable;
        }

        throw new NumberFormatException("index is out of range");
    }
}
