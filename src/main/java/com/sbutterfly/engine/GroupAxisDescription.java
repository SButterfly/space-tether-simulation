package com.sbutterfly.engine;

import com.sbutterfly.engine.trace.Axis;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author s-ermakov
 */
public class GroupAxisDescription extends ArrayList<Axis> {
    private final String name;

    public GroupAxisDescription(String name) {
        this.name = name;
    }

    public GroupAxisDescription(String name, Collection<? extends Axis> collection) {
        super(collection);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
