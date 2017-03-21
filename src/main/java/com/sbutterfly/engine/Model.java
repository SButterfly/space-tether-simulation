package com.sbutterfly.engine;

import com.sbutterfly.concurrency.CallbackFuture;
import com.sbutterfly.concurrency.ExecutionUtils;
import com.sbutterfly.differential.Differential;
import com.sbutterfly.differential.DifferentialResult;
import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.services.AppSettings;
import com.sbutterfly.utils.Func;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Базовый класс, который содержит логику для обработки и хранения дифференциальных данных модели.
 * Например, колебания маятника, простая система развертывания, система развертывания с обатной связью.
 *
 * @author s-ermakov
 */
public abstract class Model {

    private static final ExecutorService MODEL_EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    private String name = "Безымянный";
    private Color color = Color.black;

    private Map<Axis, Double> initialValues = new HashMap<>();
    private CallbackFuture<? extends ModelResult> futureResult;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Возвращает все начальные значения.
     */
    public Map<Axis, Double> getInitialValues() {
        return initialValues;
    }

    /**
     * Возвращает значение определенное по-умолчанию.
     */
    public double getInitialValue(Axis axis) {
        return initialValues.getOrDefault(axis, 0.0);
    }

    /**
     * Задает значение по умолчанию.
     */
    public void setInitialValue(Axis axis, double value) {
        initialValues.put(axis, value);
    }

    public Map<Axis, Double> getEps() {
        Differential differential = new Differential(getFunction(), getStartTimeVector(), AppSettings.getODEStep(),
                AppSettings.getODEMethod(), getExitFunction());

        Differential partDifferential = new Differential(getFunction(), getStartTimeVector(), AppSettings.getODEStep(),
                AppSettings.getODEMethod(), getExitFunction());

        DifferentialResult normalResult = differential.different();
        DifferentialResult partResult = partDifferential.different();

        TimeVector lastTimeResult = normalResult.getValues().get(normalResult.getValues().size() - 1);
        TimeVector lastTimePartResult = partResult.getValues().get(partResult.getValues().size() - 1);

        Map<Axis, Double> resultMap = new LinkedHashMap<>();
        List<Axis> functionAxises = getFunctionAxises();

        int twoInPowerOfP = (int) Math.pow(2, AppSettings.getODEMethod().getP());
        for (int i = 0, n = functionAxises.size(); i < n; i++) {
            Axis functionAxis = functionAxises.get(i);
            double value = Math.abs((lastTimePartResult.get(i) - lastTimeResult.get(i)) * twoInPowerOfP
                    / (twoInPowerOfP - 1));
            resultMap.put(functionAxis, value);
        }
        return resultMap;
    }

    /**
     * Возвращает описание модели в виде списка групп параметров, необходимых к заданию.
     */
    public abstract List<GroupAxisDescription> getModelDescription();

    protected abstract TimeVector getStartTimeVector();

    protected abstract List<Axis> getFunctionAxises();

    protected abstract Function getFunction();

    /**
     * Метод получающий инициализирующую копию результата интегрирования.
     */
    protected abstract ModelResult getInitModelResult();

    /**
     * Пересчитывает значения системы.
     */
    public void refresh() {
        futureResult = null;
        getValuesFuture();
    }

    public CallbackFuture<? extends ModelResult> getValuesFuture() {
        if (futureResult == null) {
            futureResult = ExecutionUtils.submit(MODEL_EXECUTOR_SERVICE, () -> {
                ModelResult modelResult = getInitModelResult();
                initialValues.forEach(modelResult::setInitialValue);

                Differential differential = new Differential(getFunction(), getStartTimeVector(),
                        AppSettings.getODEStep(), AppSettings.getODEMethod(),
                        getExitFunction());
                DifferentialResult differentialResult = differential.different();

                modelResult.setValues(differentialResult.getValues());
                return modelResult;
            });
        }
        return futureResult;
    }

    public void serialize(DataOutputStream stream) throws IOException {
        int size = initialValues.size();
        stream.writeInt(size);
        stream.writeUTF(name);
        stream.writeInt(color.getRGB());
        for (Map.Entry<Axis, Double> entry : initialValues.entrySet()) {
            String axisName = entry.getKey().getName();
            String humanableName = entry.getKey().getHumanReadableName();

            stream.writeUTF(axisName);
            stream.writeUTF(humanableName);
            stream.writeDouble(entry.getValue());
        }
    }

    public void deserialize(DataInputStream stream) throws IOException {
        int size = stream.readInt();
        name = stream.readUTF();
        color = new Color(stream.readInt());
        for (int i = 0; i < size; i++) {
            String axisName = stream.readUTF();
            String humanName = stream.readUTF();
            double value = stream.readDouble();

            Axis axis = new Axis(axisName, humanName);
            initialValues.put(axis, value);
        }
    }

    @SuppressWarnings("checkstyle:magicnumber")
    public Func<Boolean, TimeVector> getExitFunction() {
        final double time = AppSettings.getODETime();
        return timeVector -> timeVector.getTime() + 1e-6 >= time;
    }
}
