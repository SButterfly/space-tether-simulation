package com.sbutterfly.engine;

import java.util.LinkedList;

/**
 * Класс, модержащий набор моделей одного типа.
 *
 * @author s-ermakov
 */
public class ModelSet<T extends Model> {

    private final LinkedList<T> list = new LinkedList<>();

    public void add(T model) {
        list.add(model);
    }

    public void remove(T model) {
        list.remove(model);
    }

    public int size() {
        return list.size();
    }

    public void clear()
}
