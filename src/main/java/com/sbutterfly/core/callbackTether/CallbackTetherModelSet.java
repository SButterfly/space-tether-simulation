package com.sbutterfly.core.callbackTether;

import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelSet;
import com.sbutterfly.engine.trace.TraceDescription;

import java.util.Arrays;
import java.util.List;

import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.L_axis;
import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.Time_axis;
import static com.sbutterfly.core.callbackTether.CallbackTetherTraceService.V_axis;

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
                new TraceDescription(Time_axis(), V_axis())
        );
    }
}
