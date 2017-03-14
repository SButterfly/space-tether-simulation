package com.sbutterfly.core.callbackTether;

import com.sbutterfly.differential.TimeVector;
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

    @SuppressWarnings("all")
    protected TimeVector getStartTimeVector() {
        CallbackTetherFunction function = getFunction();

        double L = getInitialValue(CallbackTetherTraceService.L_axis());
        double V = getInitialValue(CallbackTetherTraceService.V_axis());

        double tetta = getInitialValue(CallbackTetherTraceService.Tetta_axis());
        double tetta_t = getInitialValue(CallbackTetherTraceService.Tetta_t_axis());

        double x1 = function.getX1(L, tetta);
        double y1 = function.getY1(L, tetta);

        double Vx1 = function.getVX1(V, tetta_t);
        double Vy1 = function.getVY1(V, tetta_t);

        double x2 = function.getX2(L, tetta);
        double y2 = function.getY2(L, tetta);

        double Vx2 = function.getVX2(V, tetta_t);
        double Vy2 = function.getVY2(V, tetta_t);

        double t = getInitialValue(CallbackTetherTraceService.Time_axis());

        TimeVector startVector = new TimeVector(t,
                x1, y1, Vx1, Vy1,
                x2, y2, Vx2, Vy2,
                L, V, tetta, tetta_t, L, V);

        return startVector;
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
        return new RopeModelResult(getFunction());
    }

    private class RopeModelResult extends ModelResult {

        private final CallbackTetherFunction callbackTetherFunction;

        RopeModelResult(CallbackTetherFunction callbackTetherFunction) {
            this.callbackTetherFunction = callbackTetherFunction;
        }

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
