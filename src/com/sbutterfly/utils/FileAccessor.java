package com.sbutterfly.utils;

import java.io.*;

/**
 * Created by Sergei on 21.02.2015.
 */
public class FileAccessor {

    public static void write(File file, String text) throws IOException {
        file.createNewFile();
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            writer.write(text);
        }
    }

    public static String read(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            return reader.readLine();
        }
    }
}
