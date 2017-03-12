package com.sbutterfly.gui_name.helpers;

import java.util.LinkedList;
import java.util.List;

/**
 * @author s-ermakov
 */
public class EventHandler<T> {

    private List<EventListener<T>> listeners = new LinkedList<>();

    public boolean add(EventListener<T> listener) {
        return listeners.add(listener);
    }

    public boolean remove(EventListener<T> listener) {
        return listeners.remove(listener);
    }

    public void invoke(T event) {
        listeners.forEach(l -> l.onSubmit(event));
    }
}
