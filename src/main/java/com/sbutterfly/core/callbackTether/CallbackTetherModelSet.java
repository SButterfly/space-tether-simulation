package com.sbutterfly.core.callbackTether;

import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelSet;
import com.sbutterfly.engine.trace.MultiTraceDescription;
import com.sbutterfly.engine.trace.SingleTraceDescription;
import com.sbutterfly.engine.trace.TraceDescription;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.*;
import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.X2_local_axis;
import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.Y1_local_axis;
import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.Y2_local_axis;

/**
 * @author s-ermakov
 */
public class CallbackTetherModelSet extends ModelSet {

    private static final SingleTraceDescription EMPTY_TRACE_DESCRIPTION = new SingleTraceDescription(empty(), empty(),
            "---------------------");

    @Override
    public Model createModel() {
        return new CallbackTetherModel();
    }

    @Override
    public List<TraceDescription> getModelTraces() {
        return Arrays.asList(
                new SingleTraceDescription(Time_axis(), L_axis()),
                new SingleTraceDescription(Time_axis(), V_axis()),
                new SingleTraceDescription(Time_axis(), Lp_axis()),
                new SingleTraceDescription(Time_axis(), V_p_axis()),

                new SingleTraceDescription(Time_axis(), Tetta_degress_axis()),
                new SingleTraceDescription(Time_axis(), Tetta_t_degress_axis()),
                EMPTY_TRACE_DESCRIPTION,
                new SingleTraceDescription(Time_axis(), F_power_axis(), "Управляющая сила, F"),
                new SingleTraceDescription(Time_axis(), T_axis(), "Сила натяжения, T"),
                new SingleTraceDescription(Time_axis(), Fp_power_axis(), "Номинальная сила, Fp"),
                EMPTY_TRACE_DESCRIPTION,
                new SingleTraceDescription(Time_axis(), Tether_deformation_axis(), "Деформация троса"),
                new SingleTraceDescription(Time_axis(), Tether_elongation_axis(), "Удлинение троса"),
                EMPTY_TRACE_DESCRIPTION,
                new SingleTraceDescription(Time_axis(), Point_length_axis(), "Расстояние между КА"),
                new MultiTraceDescription(Y_axis().getHumanReadableName(), X_axis().getHumanReadableName(),
                        new Pair<>(Y1_local_axis(), X1_local_axis()),
                        new Pair<>(Y2_local_axis(), X2_local_axis())
 ),
                EMPTY_TRACE_DESCRIPTION,
                new SingleTraceDescription(Time_axis(), Line_transition_process_axis(),
                        "Переходный процесс по длине"),
                new SingleTraceDescription(Time_axis(), Speed_transition_process_axis(),
                        "Переходный процесс по скорости")
        );
    }
}
