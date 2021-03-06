package com.sbutterfly.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Sergei on 21.02.2015.
 */
public class FileAccessor {

    private FileAccessor() {
    }

    public static void write(File file, String text) throws IOException {
        file.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.write(text);
        }
    }

    public static String read(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String val;
            while ((val = reader.readLine()) != null) {
                stringBuilder.append(val);
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }
}
