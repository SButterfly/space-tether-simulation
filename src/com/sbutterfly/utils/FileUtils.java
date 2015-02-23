package com.sbutterfly.utils;

import com.sbutterfly.core.ODEModelSerializer;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Created by Sergei on 21.02.2015.
 */
public class FileUtils {

    public final static String ode = "ode";
    public final static String odex = "odex";

    public final static FileNameExtensionFilter bothFilter = new FileNameExtensionFilter("Мат. модели", ode, odex);
    public final static FileNameExtensionFilter odeFilter = new FileNameExtensionFilter("Мат. модели", ode);
    public final static FileNameExtensionFilter odexFilter = new FileNameExtensionFilter("Мат. модели с расчетом", odex);

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File file) {
        String ext = "";
        String str = file.getName();
        int i = str.lastIndexOf('.');

        if (i > 0 && i < str.length() - 1) {
            ext = str.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static File setExtension(File file, String ext) {
        return new File(file.getPath() + "." + ext);
    }

    public static ODEModelSerializer.SerializeType getSerializeType(File file) {
        String extension = getExtension(file);

        if (extension.equals(ode)) {
            return ODEModelSerializer.SerializeType.Params;
        }
        if (extension.equals(odex)) {
            return ODEModelSerializer.SerializeType.ParamsAndValues;
        }
        throw new IllegalArgumentException();
    }
}