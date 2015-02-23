package com.sbutterfly.core;

import com.sbutterfly.core.pendulum.PendulumModel;

/**
 * Created by Sergei on 21.02.2015.
 */
public class ODEModelSerializer {

    private static final String separateString = "#%#";

    public static String serialize(ODEBaseModel model, SerializeType type) {

        String classValue = model.getClass().getSimpleName();
        String serObject = model.serialize();
        return classValue + separateString + serObject;
    }

    public static ODEBaseModel deserialize(String value) {

        String[] str = value.split(separateString);

        ODEBaseModel model = modelFactory(str[0]);
        model.deserialize(str[1]);
        return model;
    }

    private static ODEBaseModel modelFactory(String name) {

        if (PendulumModel.class.getSimpleName().equals(name)) {
            return new PendulumModel();
        }

        throw new IllegalArgumentException("name");
    }

    public enum SerializeType {
        @Deprecated
        Params,
        ParamsAndValues
    }

    public static interface ODESerializable {
        String serialize();

        void deserialize(String value);
    }
}
