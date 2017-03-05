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
 */
public abstract class BaseSystem implements SystemSerializer.Serializable {

    private final double[] startParamsVector = new double[paramsNames().length];
    private final double[] initialParamsVector = new double[initialParamsNames().length];
    private TimeVector[] vectors;
    private volatile Differential.DifferentialIterator iterator;

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

    public String[] initialParamsNames() {
        return new String[0];
    }

    public String[] paramsNames() {
        return new String[0];
    }

    public String[] paramsExtNames() {
        return new String[0];
    }

    public String[] customParamsNames() {
        return new String[]{"t"};
    }

    public String[] customParamsExtNames() {
        return new String[]{"с"};
    }

    public TimeVector getStartParamsVector() {
        return new TimeVector(0, startParamsVector);
    }

    public double getStartParameter(int index) {
        return startParamsVector[index];
    }

    public void setStartParameter(int index, double v) {
        if (startParamsVector[index] == v) return;
        startParamsVector[index] = v;
    }

    public double getInitialParameter(int index) {
        return initialParamsVector[index];
    }

    public void setInitialParameter(int index, double v) {
        if (initialParamsVector[index] == v) return;
        initialParamsVector[index] = v;
    }

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

    public synchronized boolean hasValues() {
        return vectors != null;
    }

    public synchronized TimeVector[] values() {
        return values(true);
    }

    public synchronized TimeVector[] values(boolean useCache) {
        if (vectors == null || !useCache) {
            Differential differential = new Differential(getFunction(), getStartParamsVector(), getODETime(), getNumberOfIterations(), getMethod());
            //TODO remove
            if (true) {
                iterator = differential.iterator();
            } else {
                iterator = differential.iterator(timeVector -> {
                    double currentLength = timeVector.get(0);
                    double maxLength = BaseSystem.this.getInitialParameter(5);
                    return currentLength + 500 >= maxLength;
                }); //останавливаемся, когда длина тросса на 0.5 км меньше запланированной
            }
            vectors = differential.makeDifferential(iterator);
            if (iterator.hasCanceled()) {
                vectors = null;
            }
        }
        return vectors;
    }

    public AdditionalLineView.Processable getProcessable() {
        return iterator;
    }

    public void setToTrace(Index yIndex, Index xIndex, ITrace2D trace2D) {
        TimeVector[] vectors = values();
        for (int i = 0, n = vectors.length; i < n; i++) {

            final double x = getValue(vectors[i], xIndex);
            final double y = getValue(vectors[i], yIndex);

            trace2D.addPoint(x, y);
        }
    }

    public double getValue(TimeVector vector, Index index) {
        Customable customable = index.getCustomable();
        return customable == null ? vector.get(index.getIndex()) : customable.customize(vector);
    }

    public String getName(Index index) {
        Customable customable = index.getCustomable();
        return customable == null ? paramsNames()[index.getIndex()] : customParamsNames()[index.getIndex()];
    }

    public String getExtName(Index index) {
        Customable customable = index.getCustomable();
        return customable == null ? paramsExtNames()[index.getIndex()] : customParamsExtNames()[index.getIndex()];
    }

    public String getFullAxisName(Index index) {
        return getName(index) + ", " + getExtName(index);
    }

    @Override
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

    @Override
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
        throw new NotImplementedException();
    }
}
