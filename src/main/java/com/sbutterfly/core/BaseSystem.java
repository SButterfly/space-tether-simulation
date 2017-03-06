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

    public Customable getCustomable(int index) {
        if (index != 0) throw new NumberFormatException("Index is out of diapason");
        return new Customable() {
            @Override
            public double customize(TimeVector vector) {
                return vector.getTime();
            }
        };
    }

    public Vector getEps(ODEMethod method, double time, double h) {
        Differential differential = new Differential(getFunction(),
            getStartParamsVector(),
            time,
            (int) (time / h),
            method);
        TimeVector lastH = null;
        for (TimeVector vector : differential) {
            lastH = vector;
            if (lastH.isAllNaN())
                break;
        }
        differential = new Differential(getFunction(),
            getStartParamsVector(),
            time,
            (int) (time * 2 / h),
            method);
        TimeVector lastH2 = null;
        for (TimeVector vector : differential) {
            lastH2 = vector;
            if (lastH2.isAllNaN())
                break;
        }

        double[] result = new double[lastH.size()];
        int _2p = 1 << method.getP();
        for (int i = 0, n = result.length; i < n; i++) {
            result[i] = Math.abs((lastH2.get(i) - lastH.get(i)) * _2p / (_2p - 1));
        }
        return new Vector(result);
    }

    public boolean hasValues() {
        return vectors != null;
    }

    public DifferentialResult values() {
        return values(true);
    }

    public DifferentialResult values(boolean useCache) {
        if (vectors == null || !useCache) {
            Differential differential = new Differential(getFunction(), getStartParamsVector(), 0.0,
                    getODETime(), getNumberOfIterations(), getMethod());
            vectors = differential.different();
        }
        return vectors;
    }
    public double getValue(TimeVector vector, Index index) {
        Customable customable = index.getCustomable();
        return customable == null ? vector.get(index.getIndex()) : customable.customize(vector);
    }

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

    public void clear() {
        vectors = null;
    }

    public abstract Function getFunction();

    public double getODETime() {
        return AppSettings.getODETime();
    }

    public int getNumberOfIterations() {
        return (int) (getODETime() / AppSettings.getODEStep());
    }

    public ODEMethod getMethod() {
        return AppSettings.getODEMethod();
    }
}
