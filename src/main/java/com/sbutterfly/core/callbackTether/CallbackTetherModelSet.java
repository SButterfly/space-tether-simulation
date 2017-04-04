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

/**
 * @author s-ermakov
 */
public class CallbackTetherModelSet extends ModelSet {

    private static final SingleTraceDescription EMPTY_TRACE_DESCRIPTION = new SingleTraceDescription(empty(), empty(),
            "----------------");

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
                new SingleTraceDescription(Time_axis(), F_power_axis(), "Управляющая сила"),
                new SingleTraceDescription(Time_axis(), T_axis(), "Сила натяжения"),
                new SingleTraceDescription(Time_axis(), Fp_power_axis(), "Номинальная сила"),
                EMPTY_TRACE_DESCRIPTION,
                new SingleTraceDescription(Time_axis(), Tether_deformation_axis(), "Деформация троса"),
                new SingleTraceDescription(Time_axis(), Tether_elongation_axis(), "Удлинение троса"),
                EMPTY_TRACE_DESCRIPTION,
                new MultiTraceDescription(X_axis().getHumanReadableName(), Y_axis().getHumanReadableName(),
                        new Pair<>(X1_axis(), Y1_axis()),
                        new Pair<>(X2_axis(), Y2_axis())),
                EMPTY_TRACE_DESCRIPTION,
                new SingleTraceDescription(Time_axis(), Line_transition_process_axis(), "Переходный процесс по длине"),
                new SingleTraceDescription(Time_axis(), Speed_transition_process_axis(), "Переходный процесс по скорости")
        );
    }
}
