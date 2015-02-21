package com.sbutterfly.core;

import com.sbutterfly.pendulum.PendulumModel;

/**
 * Created by Sergei on 21.02.2015.
 */
public class ODEModelSerializer {

    public static String serialize(ODEBaseModel model, SerializeType type) {
        //FIXME implement
        return "";
    }

    public static ODEBaseModel deserialize(String value) {
        //FIXME implement
        return new PendulumModel();
    }

    public enum SerializeType {
        Params,
        ParamsAndValues
    }
}
