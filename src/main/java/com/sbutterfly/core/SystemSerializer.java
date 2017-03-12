package com.sbutterfly.core;

import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelSet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Sergei on 21.02.2015.
 */
public class SystemSerializer {

    private SystemSerializer() {
    }

    public static void serialize(ModelSet modelSet, DataOutputStream dataOutputStream) throws IOException {
        String classValue = modelSet.getClass().getCanonicalName();
        dataOutputStream.writeUTF(classValue);
        dataOutputStream.writeInt(modelSet.size());
        for (Model model : modelSet) {
            model.serialize(dataOutputStream);
        }
    }

    public static ModelSet deserialize(DataInputStream dataInputStream) throws IOException {
        String className = dataInputStream.readUTF();
        ModelSet modelSet;

        try {
            Class<?> clazz = Class.forName(className);
            modelSet = (ModelSet) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        int size = dataInputStream.readInt();
        for (int i = 0; i < size; i++) {
            Model model = modelSet.createModel();
            model.deserialize(dataInputStream);
            modelSet.add(model);
        }
        return modelSet;
    }
}
