package com.sbutterfly.services;

import com.sbutterfly.core.callbackTether.CallbackTetherModelSet;
import com.sbutterfly.engine.ModelSet;

/**
 * @author s-ermakov
 */
public class ModelSetFactory {
    private ModelSetFactory() {
    }

    public static ModelSet createNewModelSet() {
//        return new TetherModelSet();
        return new CallbackTetherModelSet();
    }
}
