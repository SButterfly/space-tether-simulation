package com.sbutterfly.differential;

import java.util.List;

/**
 * @author s-ermakov
 */
public class DifferentialResult {

    private final List<TimeVector> values;

    public DifferentialResult(List<TimeVector> values) {
        this.values = values;
    }

    public List<TimeVector> getValues() {
        return values;
    }

    public TimeVector getTimeVector(double time) {
        return values.stream()
                .filter(tv -> tv.getTime() == time)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Failed to find vector if time: " + time));
    }
}
