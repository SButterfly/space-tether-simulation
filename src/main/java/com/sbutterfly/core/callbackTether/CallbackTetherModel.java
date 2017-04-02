package com.sbutterfly.core.callbackTether;

import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.engine.GroupAxisDescription;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelResult;
import com.sbutterfly.engine.trace.Axis;

import java.util.Arrays;
import java.util.List;

import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.Power_transition_process_axis;
import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.Speed_transition_process_axis;
import static java.lang.Math.abs;

/**
 * @author s-ermakov
 */
@SuppressWarnings("checkstyle:magicnumber")
public class CallbackTetherModel extends Model {

    private static final List<Axis> CONSTANT_VALUES = Arrays.asList(
            CallbackTetherTraceService.M1_axis(),
            CallbackTetherTraceService.M2_axis(),
            CallbackTetherTraceService.M3_axis(),
            CallbackTetherTraceService.A_axis(),
            CallbackTetherTraceService.B_axis()
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
            CallbackTetherTraceService.Tetta_p_axis(),
            CallbackTetherTraceService.Tettat_p_axis(),
            CallbackTetherTraceService.Lp_axis(),
            CallbackTetherTraceService.V_p_axis()
    );

    private static final GroupAxisDescription ROPE_GROUP = new GroupAxisDescription("Параметры тросовой системы",
            Arrays.asList(
                    CallbackTetherTraceService.M1_axis(),
                    CallbackTetherTraceService.M2_axis(),
                    CallbackTetherTraceService.M3_axis(),
                    CallbackTetherTraceService.Lk_axis()
            )
    );

    private static final GroupAxisDescription SYSTEM_GROUP = new GroupAxisDescription("Параметры закона",
            Arrays.asList(
                    CallbackTetherTraceService.A_axis(),
                    CallbackTetherTraceService.B_axis()
            )
    );

    private static final GroupAxisDescription CALLBACK_GROUP = new GroupAxisDescription("Коэффициенты обратной связи",
            Arrays.asList(
                    CallbackTetherTraceService.KL_axis(),
                    CallbackTetherTraceService.KV_axis()
            )
    );

    private static final GroupAxisDescription START_PARAMS_GROUP = new GroupAxisDescription("Начальные параметры",
            Arrays.asList(
                    CallbackTetherTraceService.Lp_axis(),
                    CallbackTetherTraceService.V_p_axis(),

                    CallbackTetherTraceService.L_axis(),
                    CallbackTetherTraceService.V_axis(),

                    CallbackTetherTraceService.Tetta_p_degress_axis(),
                    CallbackTetherTraceService.Tetta_t_p_degress_axis(),

                    CallbackTetherTraceService.Tetta_degress_axis(),
                    CallbackTetherTraceService.Tetta_t_degress_axis()
            )
    );

    public CallbackTetherModel() {
        setInitialValue(CallbackTetherTraceService.L_axis(), 1);
        setInitialValue(CallbackTetherTraceService.V_axis(), 2.5);
        setInitialValue(CallbackTetherTraceService.Lp_axis(), 1);
        setInitialValue(CallbackTetherTraceService.V_p_axis(), 2.5);

        setInitialValue(CallbackTetherTraceService.M1_axis(), 50);
        setInitialValue(CallbackTetherTraceService.M2_axis(), 100);
        setInitialValue(CallbackTetherTraceService.M3_axis(), 0.2);

        setInitialValue(CallbackTetherTraceService.A_axis(), 4.7);
        setInitialValue(CallbackTetherTraceService.B_axis(), 4);

        setInitialValue(CallbackTetherTraceService.Lk_axis(), 3000);
        setInitialValue(CallbackTetherTraceService.KL_axis(), 1);
        setInitialValue(CallbackTetherTraceService.KV_axis(), 1);
    }

    @Override
    public List<GroupAxisDescription> getModelDescription() {
        return Arrays.asList(ROPE_GROUP, SYSTEM_GROUP, CALLBACK_GROUP, START_PARAMS_GROUP);
    }

    @SuppressWarnings("all")
    protected TimeVector getStartTimeVector() {
        CallbackTetherFunction function = getFunction();

        double L = getInitialValue(CallbackTetherTraceService.L_axis());
        double V = getInitialValue(CallbackTetherTraceService.V_axis());

        double Lp = getInitialValue(CallbackTetherTraceService.Lp_axis());
        double Vp = getInitialValue(CallbackTetherTraceService.V_p_axis());

        double tetta = getInitialValue(CallbackTetherTraceService.Tetta_degress_axis()) * Math.PI / 180d;
        double tetta_t = getInitialValue(CallbackTetherTraceService.Tetta_t_degress_axis()) * Math.PI / 180d;

        double tetta_p = getInitialValue(CallbackTetherTraceService.Tetta_p_degress_axis()) * Math.PI / 180d;
        double tetta_t_p = getInitialValue(CallbackTetherTraceService.Tetta_t_p_degress_axis()) * Math.PI / 180d;

        double x1 = function.getX1(L, tetta);
        double y1 = function.getY1(L, tetta);

        double Vx1 = function.getVX1(V, tetta_t);
        double Vy1 = function.getVY1(V, tetta_t);

        double x2 = function.getX2(L, tetta);
        double y2 = function.getY2(L, tetta);

        double Vx2 = function.getVX2(V, tetta_t);
        double Vy2 = function.getVY2(V, tetta_t);

        // Проверяем, что полученные координаты образуют верные значения
        double length = function.length(x1 - x2, y1 - y2);
        assert abs(length - L) <= 1e-9;
        double speed = function.length(x1 - x2, y1 - y2);
        assert abs(speed - V) <= 1e-9;

        double t = getInitialValue(CallbackTetherTraceService.Time_axis());

        TimeVector startVector = new TimeVector(t,
                x1, y1, Vx1, Vy1,
                x2, y2, Vx2, Vy2,
                L, V, tetta_p, tetta_t_p, Lp, Vp);

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

        final double lk = getInitialValue(CallbackTetherTraceService.Lk_axis());

        return new CallbackTetherFunction(m1, m2, m3, KL, KV, a, b, lk);
    }

//    @Override
//    public Func<Boolean, TimeVector> getExitFunction() {
//        CallbackTetherFunction callbackTetherFunction = getFunction();
//        Func<Boolean, TimeVector> lengthFunction = callbackTetherFunction.getExitFunction();
//        Func<Boolean, TimeVector> timeFunction = super.getExitFunction();
//        return timeVector -> lengthFunction.invoke(timeVector) || timeFunction.invoke(timeVector);
//    }

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

            if (axis == CallbackTetherTraceService.Tetta_degress_axis()) {
                double tettaRad = getValue(timeVector, CallbackTetherTraceService.Tetta_p_axis());
                return tettaRad * 180 / Math.PI;
            }

            if (axis == CallbackTetherTraceService.Tetta_t_degress_axis()) {
                double tettaTRad = getValue(timeVector, CallbackTetherTraceService.Tettat_p_axis());
                return tettaTRad * 180 / Math.PI;
            }

            if (axis == CallbackTetherTraceService.Time_axis()) {
                return timeVector.getTime();
            }

            if (axis == CallbackTetherTraceService.Line_transition_process_axis()) {
                double L = getValue(timeVector, CallbackTetherTraceService.L_axis());
                double Lp = getValue(timeVector, CallbackTetherTraceService.Lp_axis());

                return L - Lp;
            }

            if (axis == CallbackTetherTraceService.Point_length_axis()) {
                double x1 = getValue(timeVector, CallbackTetherTraceService.X1_axis());
                double x2 = getValue(timeVector, CallbackTetherTraceService.X2_axis());
                double y1 = getValue(timeVector, CallbackTetherTraceService.Y1_axis());
                double y2 = getValue(timeVector, CallbackTetherTraceService.Y2_axis());

                return callbackTetherFunction.length(x1 - x2, y1 - y2);
            }

            if (axis == CallbackTetherTraceService.Tether_deformation_axis()) {
                double pointLength = getValue(timeVector, CallbackTetherTraceService.Point_length_axis());
                double L = getValue(timeVector, CallbackTetherTraceService.L_axis());
                return pointLength / L;
            }

            if (axis == CallbackTetherTraceService.Tether_elongation_axis()) {
                double pointLength = getValue(timeVector, CallbackTetherTraceService.Point_length_axis());
                double L = getValue(timeVector, CallbackTetherTraceService.L_axis());
                return pointLength - L;
            }

            if (axis == CallbackTetherTraceService.F_power_axis()) {
                double L = getValue(timeVector, CallbackTetherTraceService.L_axis());
                double Lt = getValue(timeVector, CallbackTetherTraceService.V_axis());
                double Lp = getValue(timeVector, CallbackTetherTraceService.Lp_axis());
                double Ltp = getValue(timeVector, CallbackTetherTraceService.V_p_axis());

                return callbackTetherFunction.Fc(L, Lt, Lp, Ltp);
            }

            if (axis == CallbackTetherTraceService.Fp_power_axis()) {
                double Lp = getValue(timeVector, CallbackTetherTraceService.Lp_axis());
                double Ltp = getValue(timeVector, CallbackTetherTraceService.V_p_axis());

                return callbackTetherFunction.Fcn(Lp, Ltp);
            }

            if (axis == CallbackTetherTraceService.T_axis()) {
                double x1 = getValue(timeVector, CallbackTetherTraceService.X1_axis());
                double x2 = getValue(timeVector, CallbackTetherTraceService.X2_axis());
                double y1 = getValue(timeVector, CallbackTetherTraceService.Y1_axis());
                double y2 = getValue(timeVector, CallbackTetherTraceService.Y2_axis());

                double L = getValue(timeVector, CallbackTetherTraceService.L_axis());

                return callbackTetherFunction.T(x1, y1, x2, y2, L);
            }

            if (axis == Speed_transition_process_axis()) {
                double Lt = getValue(timeVector, CallbackTetherTraceService.V_axis());
                double Ltp = getValue(timeVector, CallbackTetherTraceService.V_p_axis());

                return Lt - Ltp;
            }

            if (axis == Power_transition_process_axis()) {
                double f = getValue(timeVector, CallbackTetherTraceService.F_power_axis());
                double fp = getValue(timeVector, CallbackTetherTraceService.Fp_power_axis());

                return f - fp;
            }

            if (axis == CallbackTetherTraceService.X_axis()) {
                return -Math.sin(timeVector.get(2)) * timeVector.get(0);
            }

            if (axis == CallbackTetherTraceService.Y_axis()) {
                return -Math.cos(timeVector.get(2)) * timeVector.get(0);
            }

            if (axis == CallbackTetherTraceService.empty()) {
                return 0;
            }

            throw new IllegalArgumentException("Unsupported axis: " + axis);
        }
    }
}
