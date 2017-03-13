package com.sbutterfly.core.callbackTether;

import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.engine.GroupAxisDescription;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelResult;
import com.sbutterfly.engine.trace.Axis;

import java.util.Arrays;
import java.util.List;

/**
 * @author s-ermakov
 */
@SuppressWarnings("magicnumber")
public class CallbackTetherModel extends Model {

    private static final List<Axis> CONSTANT_VALUES = Arrays.asList(
            CallbackTetherTraceService.M1_axis(),
            CallbackTetherTraceService.M2_axis(),
            CallbackTetherTraceService.M3_axis(),
            CallbackTetherTraceService.A_axis(),
            CallbackTetherTraceService.B_axis(),
            CallbackTetherTraceService.C_axis()
    );

    private static final List<Axis> START_VALUES = Arrays.asList(
            CallbackTetherTraceService.X1_axis(),
            CallbackTetherTraceService.Y1_axis(),
            CallbackTetherTraceService.VX1_axis(),
            CallbackTetherTraceService.VY1_axis(),

            CallbackTetherTraceService.X2_axis(),
            CallbackTetherTraceService.Y2_axis(),
            CallbackTetherTraceService.VX2_axis(),
            CallbackTetherTraceService.VY2_axis(),

            CallbackTetherTraceService.L_axis(),
            CallbackTetherTraceService.V_axis(),
            CallbackTetherTraceService.Tetta_axis(),
            CallbackTetherTraceService.Tetta_t_axis(),
            CallbackTetherTraceService.L_axis(),
            CallbackTetherTraceService.V_axis()
    );

    public CallbackTetherModel() {
        setInitialValue(CallbackTetherTraceService.L_axis(), 1);
        setInitialValue(CallbackTetherTraceService.V_axis(), 2.5);
        setInitialValue(CallbackTetherTraceService.Tetta_axis(), 0.0);

        setInitialValue(CallbackTetherTraceService.M1_axis(), 40);
        setInitialValue(CallbackTetherTraceService.M2_axis(), 20);
        setInitialValue(CallbackTetherTraceService.M3_axis(), 0.2);

        setInitialValue(CallbackTetherTraceService.A_axis(), 4);
        setInitialValue(CallbackTetherTraceService.B_axis(), 5);
        setInitialValue(CallbackTetherTraceService.C_axis(), 5);

        setInitialValue(CallbackTetherTraceService.Lk_axis(), 3000);
        setInitialValue(CallbackTetherTraceService.KL_axis(), 1);
        setInitialValue(CallbackTetherTraceService.KV_axis(), 1);
    }

    @Override
    public List<GroupAxisDescription> getModelDescription() {
        return CallbackTetherTraceService.getGroupAxisDescriptions();
    }

    protected TimeVector getStartTimeVector() {
        Vector valuesVector = new Vector(START_VALUES.size());
        for (int i = 0; i < START_VALUES.size(); i++) {
            valuesVector.set(i, getInitialValue(START_VALUES.get(i)));
        }

        double t = getInitialValue(CallbackTetherTraceService.Time_axis());
        return new TimeVector(t, valuesVector);
    }

    @Override
    protected List<Axis> getFunctionAxises() {
        return START_VALUES;
    }

    @Override
    @SuppressWarnings("checkstyle:all")
    protected CallbackTetherFunction getFunction() {
        final double m1 = getInitialValue(CallbackTetherTraceService.M1_axis());
        final double m2 = getInitialValue(CallbackTetherTraceService.M2_axis());
        final double m3 = getInitialValue(CallbackTetherTraceService.M3_axis());

        final double KL = getInitialValue(CallbackTetherTraceService.KL_axis());
        final double KV = getInitialValue(CallbackTetherTraceService.KV_axis());

        final double a = getInitialValue(CallbackTetherTraceService.A_axis());
        final double b = getInitialValue(CallbackTetherTraceService.B_axis());
        final double cc = getInitialValue(CallbackTetherTraceService.C_axis());

        final double lk = getInitialValue(CallbackTetherTraceService.Lk_axis());

        return new CallbackTetherFunction(m1, m2, m3, KL, KV, a, b, cc, lk);
    }

    @Override
    protected ModelResult getInitModelResult() {
        return new RopeModelResult();
    }

    private class RopeModelResult extends ModelResult {

        @Override
        public double getValue(TimeVector timeVector, Axis axis) {
            if (CONSTANT_VALUES.contains(axis)) {
                return CallbackTetherModel.super.getInitialValue(axis);
            }

            if (START_VALUES.contains(axis)) {
                return timeVector.get(START_VALUES.indexOf(axis));
            }

            if (axis == CallbackTetherTraceService.Time_axis()) {
                return timeVector.getTime();
            }

            if (axis == CallbackTetherTraceService.X_axis()) {
                return -Math.sin(timeVector.get(2)) * timeVector.get(0);
            }

            if (axis == CallbackTetherTraceService.Y_axis()) {
                return -Math.cos(timeVector.get(2)) * timeVector.get(0);
            }

            throw new IllegalArgumentException("Unsupported axis: " + axis);
        }
    }
}
