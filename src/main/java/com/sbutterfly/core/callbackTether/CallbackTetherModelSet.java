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

                new TraceDescription(Time_axis(), Line_transition_process_axis(), "Переходный процесс по длине"),
                new TraceDescription(Time_axis(), Tether_deformation_axis(), "Деформация троса"),
                new TraceDescription(Time_axis(), Tether_elongation_axis(), "Удлинение троса")

                // add удлинение в метрах



//                new TraceDescription(Time_axis(), Speed_transition_process_axis(), "Переходный процесс по скорости"),
//                new TraceDescription(Time_axis(), F_axis()),
//                new TraceDescription(Time_axis(), T_axis()),

//                new TraceDescription(Y1_axis(), X1_axis(), "Траектория малого КА относительно вертикали"),
//                new TraceDescription(X1_axis(), Y1_axis())
        );
    }
}
