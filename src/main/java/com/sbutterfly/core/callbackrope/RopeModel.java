package com.sbutterfly.core.callbackrope;

import com.sbutterfly.core.rope.RopeFunction;
import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.engine.GroupAxisDescription;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.trace.Axis;

import java.util.Arrays;
import java.util.List;

import static com.sbutterfly.core.callbackrope.RopeTraceService.A_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.B_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Betta_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Betta_t_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.H_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.L_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Lk_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.M1_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.M2_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Ro_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.T_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Tetta_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Tetta_t_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Time_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.V_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.X_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Y_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.getGroupAxisDescriptions;

/**
 * @author s-ermakov
 */
@SuppressWarnings("magicnumber")
public class RopeModel extends Model {

    private static final List<Axis> CONSTANT_VALUES = Arrays.asList(
            M1_axis(), M2_axis(), Ro_axis(), A_axis(), B_axis(), Lk_axis(), H_axis()
    );

    private static final List<Axis> START_VALUES = Arrays.asList(
            L_axis(), V_axis(), Tetta_axis(), Tetta_t_axis(), Betta_axis(), Betta_t_axis()
    );

    public RopeModel() {
        setInitialValue(L_axis(), 1);
        setInitialValue(V_axis(), 2.5);

        setInitialValue(M1_axis(), 60);
        setInitialValue(M2_axis(), 20);
        setInitialValue(Ro_axis(), 0.0002);
        setInitialValue(A_axis(), 4);
        setInitialValue(B_axis(), 5);
        setInitialValue(Lk_axis(), 30000);
        setInitialValue(H_axis(), 1000);
    }

    @Override
    public List<GroupAxisDescription> getModelDescription() {
        return getGroupAxisDescriptions();
    }

    protected TimeVector getStartTimeVector() {
        Vector valuesVector = new Vector(START_VALUES.size());
        for (int i = 0; i < START_VALUES.size(); i++) {
            valuesVector.set(i, getInitialValue(START_VALUES.get(i)));
        }

        double t = getInitialValue(Time_axis());
        return new TimeVector(t, valuesVector);
    }

    @Override
    protected RopeFunction getFunction() {
        final double m1 = getInitialValue(M1_axis());
        final double m2 = getInitialValue(M2_axis());
        final double p = getInitialValue(Ro_axis());
        final double a = getInitialValue(A_axis());
        final double b = getInitialValue(B_axis());
        final double lk = getInitialValue(Lk_axis());
        final double h = getInitialValue(H_axis());

        return new RopeFunction(m1, m2, p, a, b, lk, h);
    }

    @Override
    protected double getValue(TimeVector timeVector, Axis axis) {
        if (CONSTANT_VALUES.contains(axis)) {
            // TODO бага, начальное значение может поменяться после срабатывания интегрирования
            return getInitialValue(axis);
        }

        if (START_VALUES.contains(axis)) {
            return timeVector.get(START_VALUES.indexOf(axis));
        }

        if (axis == Time_axis()) {
            return timeVector.getTime();
        }

        if (axis == X_axis()) {
            return -Math.sin(timeVector.get(2)) * timeVector.get(0);
        }

        if (axis == Y_axis()) {
            return -Math.cos(timeVector.get(2)) * timeVector.get(0);
        }

        if (axis == T_axis()) {
            // TODO бага, начальное значение может поменяться после срабатывания интегрирования
            return getFunction().T(timeVector.get(0), timeVector.get(1));
        }

        throw new IllegalArgumentException("Unsupported axis: " + axis);
    }
}
