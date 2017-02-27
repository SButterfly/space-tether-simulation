package com.sbutterfly.core;

import com.sbutterfly.core.rope.RopeSystem;

/**
 * Created by Sergei on 21.02.2015.
 */
public class SystemSerializer {

    private static final String NAME_SEPARATOR = "#%#";

    private SystemSerializer() {
    }

    public static String serialize(BaseSystem model) {
        String classValue = model.getClass().getSimpleName();
        String serObject = model.serialize();
        return classValue + NAME_SEPARATOR + serObject;
    }

    public static BaseSystem deserialize(String value) {
        String[] str = value.split(NAME_SEPARATOR);

        BaseSystem model = modelFactory(str[0]);
        model.deserialize(str[1]);
        return model;
    }

    private static BaseSystem modelFactory(String name) {
        if (RopeSystem.class.getSimpleName().equals(name)) {
            return new RopeSystem();
        }

        throw new IllegalArgumentException("name");
    }

    public enum SerializeType {
        @Deprecated
        Params
    }

    public interface Serializable {
        String serialize();

        void deserialize(String value);
    }
}
