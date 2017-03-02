package com.sbutterfly.core;

import com.sbutterfly.core.rope.RopeSystem;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelSet;

import java.io.StringReader;

/**
 * Created by Sergei on 21.02.2015.
 */
public class SystemSerializer {

    private static final String NAME_SEPARATOR = "#%#";

    private SystemSerializer() {
    }

    public static String serialize(ModelSet modelSet) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(modelSet.size());
        modelSet.forEach(m -> serialize(m, stringBuilder));
        return stringBuilder.toString();
    }

    public static ModelSet deserialize(String value) {
        String[] str = value.split(NAME_SEPARATOR);

        BaseSystem model = modelFactory(str[0]);
        model.deserialize(str[1]);
        return model;
    }

    private static void serialize(Model model, StringBuilder stringBuilder) {
        String classValue = model.getClass().getSimpleName();
        String serObject = model.serialize();
        return classValue + NAME_SEPARATOR + serObject;
    }

    private static Model deserialize(StringReader stringReader) {

    }

    private static BaseSystem modelFactory(String name) {
        if (RopeSystem.class.getSimpleName().equals(name)) {
            return new RopeSystem();
        }

        throw new IllegalArgumentException("name");
    }

    public interface Serializable {
        String serialize();

        void deserialize(String value);
    }
}
