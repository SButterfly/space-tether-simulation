package com.sbutterfly.core.callbackrope;

import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelSet;
import com.sbutterfly.engine.trace.TraceDescription;

import java.util.Arrays;
import java.util.List;

import static com.sbutterfly.core.callbackrope.RopeTraceService.L_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.Time_axis;
import static com.sbutterfly.core.callbackrope.RopeTraceService.V_axis;

/**
 * @author s-ermakov
 */
public class RopeModelSet extends ModelSet {

    @Override
    public Model createModel() {
        return new RopeModel();
    }

    @Override
    public List<TraceDescription> getModelTraces() {
        return Arrays.asList(
                new TraceDescription(Time_axis(), L_axis()),
                new TraceDescription(Time_axis(), V_axis())
        );
    }
}
