package com.sbutterfly.utils;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Created by Sergei on 21.02.2015.
 */
public class FileUtils {

    public static final String ODE = "ode";
    public static final String ODEX = "odex";

    public static final FileNameExtensionFilter ODE_FILTER = new FileNameExtensionFilter("Мат. модели", ODE);

    private FileUtils() {
    }

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
}
