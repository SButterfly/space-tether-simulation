package com.sbutterfly.core.callbackTether;

import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelSet;
import com.sbutterfly.engine.trace.TraceDescription;

import java.util.Arrays;
import java.util.List;

import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.*;

/**
 * @author s-ermakov
 */
public class CallbackTetherModelSet extends ModelSet {

    @Override
    public Model createModel() {
        return new CallbackTetherModel();
    }

    @Override
    public List<TraceDescription> getModelTraces() {
        return Arrays.asList(
                new TraceDescription(Time_axis(), L_axis()),
                new TraceDescription(Time_axis(), V_axis()),
                new TraceDescription(Time_axis(), Lp_axis()),
                new TraceDescription(Time_axis(), V_p_axis()),

                new TraceDescription(Time_axis(), Tetta_p_axis()),
                new TraceDescription(Time_axis(), Tettat_p_axis()),

                new TraceDescription(Time_axis(), F_power_axis(), "Управляющая сила"),
                new TraceDescription(Time_axis(), T_axis(), "Силая натяжения"),
                new TraceDescription(Time_axis(), Fp_power_axis(), "Номинальная сила"),

                new TraceDescription(Time_axis(), Line_transition_process_axis(), "Переходный процесс по длине"),
                new TraceDescription(Time_axis(), Tether_deformation_axis(), "Деформация троса"),
                new TraceDescription(Time_axis(), Tether_elongation_axis(), "Удлинение троса"),

                new TraceDescription(Time_axis(), Speed_transition_process_axis(), "Переходный процесс по скорости")
        );
    }
}
