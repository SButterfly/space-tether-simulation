package com.sbutterfly.core.tether;

import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelSet;
import com.sbutterfly.engine.trace.SingleTraceDescription;
import com.sbutterfly.engine.trace.TraceDescription;

import java.util.Arrays;
import java.util.List;

/**
 * @author s-ermakov
 */
public class TetherModelSet extends ModelSet {

    @Override
    public Model createModel() {
        return new TetherModel();
    }

    @Override
    public List<TraceDescription> getModelTraces() {
        return Arrays.asList(
                new SingleTraceDescription(TetherTraceService.Time_axis(), TetherTraceService.L_axis()),
                new SingleTraceDescription(TetherTraceService.Time_axis(), TetherTraceService.V_axis())
        );
    }
}
