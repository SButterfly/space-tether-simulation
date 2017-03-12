package com.sbutterfly.services;

import com.sbutterfly.core.tether.TetherModelSet;
import com.sbutterfly.engine.ModelSet;

/**
 * @author s-ermakov
 */
public class ModelSetFactory {
    private ModelSetFactory() {
    }

    public static ModelSet createNewModelSet() {
        return new TetherModelSet();
    }
}
