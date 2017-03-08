package com.sbutterfly.core;

import com.sbutterfly.gui.AdditionalLineView;
import com.sbutterfly.differential.*;
import com.sbutterfly.services.AppSettings;
import info.monitorenter.gui.chart.ITrace2D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.StringTokenizer;

/**
 * Базовый класс, описывающий необходимую нам систему. Например,
 * колебания маятника, простая система развертывания, система развертывания с обатной связью.
 *
 * @author s-ermakov
 * @deprecated Use Model
 */
@Deprecated
public abstract class BaseSystem {

    // ----------

    public String serialize() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(startParamsVector.length);
        stringBuilder.append('\n');
        for (int i = 0, n = startParamsVector.length; i < n; i++) {
            stringBuilder.append(getStartParameter(i));
            stringBuilder.append(' ');
        }
        stringBuilder.append('\n');

        stringBuilder.append(initialParamsVector.length);
        stringBuilder.append('\n');
        for (int i = 0, n = initialParamsVector.length; i < n; i++) {
            stringBuilder.append(getInitialParameter(i));
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
    }

    public void deserialize(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value);

        int startParamsSize = Integer.parseInt(tokenizer.nextToken());
        for (int i = 0; i < startParamsSize; i++) {
            double val = Double.parseDouble(tokenizer.nextToken());
            setStartParameter(i, val);
        }

        int initialParamsSize = Integer.parseInt(tokenizer.nextToken());
        for (int j = 0; j < initialParamsSize; j++) {
            double val = Double.parseDouble(tokenizer.nextToken());
            setInitialParameter(j, val);
        }
    }
}
