package com.sbutterfly.core.tether;

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
public class TetherModel extends Model {

    private static final List<Axis> CONSTANT_VALUES = Arrays.asList(
            TetherTraceService.M1_axis(), TetherTraceService.M2_axis(), TetherTraceService.Ro_axis(), TetherTraceService.A_axis(), TetherTraceService.B_axis(), TetherTraceService.Lk_axis(), TetherTraceService.H_axis()
    );

    private static final List<Axis> START_VALUES = Arrays.asList(
            TetherTraceService.L_axis(), TetherTraceService.V_axis(), TetherTraceService.Tetta_axis(), TetherTraceService.Tetta_t_axis(), TetherTraceService.Betta_axis(), TetherTraceService.Betta_t_axis()
    );

    public TetherModel() {
        setInitialValue(TetherTraceService.L_axis(), 1);
        setInitialValue(TetherTraceService.V_axis(), 2.5);

        setInitialValue(TetherTraceService.M1_axis(), 60);
        setInitialValue(TetherTraceService.M2_axis(), 20);
        setInitialValue(TetherTraceService.Ro_axis(), 0.0002);
        setInitialValue(TetherTraceService.A_axis(), 4);
        setInitialValue(TetherTraceService.B_axis(), 5);
        setInitialValue(TetherTraceService.Lk_axis(), 30000);
        setInitialValue(TetherTraceService.H_axis(), 1000);
    }

    @Override
    public List<GroupAxisDescription> getModelDescription() {
        return TetherTraceService.getGroupAxisDescriptions();
    }

    protected TimeVector getStartTimeVector() {
        Vector valuesVector = new Vector(START_VALUES.size());
        for (int i = 0; i < START_VALUES.size(); i++) {
            valuesVector.set(i, getInitialValue(START_VALUES.get(i)));
        }

        double t = getInitialValue(TetherTraceService.Time_axis());
        return new TimeVector(t, valuesVector);
    }

    @Override
    protected List<Axis> getFunctionAxises() {
        return START_VALUES;
    }

    @Override
    protected TetherFunction getFunction() {
        final double m1 = getInitialValue(TetherTraceService.M1_axis());
        final double m2 = getInitialValue(TetherTraceService.M2_axis());
        final double p = getInitialValue(TetherTraceService.Ro_axis());
        final double a = getInitialValue(TetherTraceService.A_axis());
        final double b = getInitialValue(TetherTraceService.B_axis());
        final double lk = getInitialValue(TetherTraceService.Lk_axis());
        final double h = getInitialValue(TetherTraceService.H_axis());

        return new TetherFunction(m1, m2, p, a, b, lk, h);
    }

    @Override
    protected ModelResult getInitModelResult() {
        return new RopeModelResult();
    }

    private class RopeModelResult extends ModelResult {

        @Override
        public double getValue(TimeVector timeVector, Axis axis) {
            if (CONSTANT_VALUES.contains(axis)) {
                return TetherModel.super.getInitialValue(axis);
            }

            if (START_VALUES.contains(axis)) {
                return timeVector.get(START_VALUES.indexOf(axis));
            }

            if (axis == TetherTraceService.Time_axis()) {
                return timeVector.getTime();
            }

            if (axis == TetherTraceService.X_axis()) {
                return -Math.sin(timeVector.get(2)) * timeVector.get(0);
            }

            if (axis == TetherTraceService.Y_axis()) {
                return -Math.cos(timeVector.get(2)) * timeVector.get(0);
            }

            if (axis == TetherTraceService.T_axis()) {
                return TetherModel.this.getFunction().T(timeVector.get(0), timeVector.get(1));
            }

            throw new IllegalArgumentException("Unsupported axis: " + axis);
        }
    }
}
