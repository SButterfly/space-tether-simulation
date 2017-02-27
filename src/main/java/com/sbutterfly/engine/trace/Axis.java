package com.sbutterfly.engine.trace;

/**
 * Класс, описывающий названия осей.
 *
 * @author s-ermakov
 */
public class Axis {
    
    private final String name;
    private final String humanReadableName;

    public Axis(String name, String humanReadableName) {
        this.name = name;
        this.humanReadableName = humanReadableName;
    }

    public String getName() {
        return name;
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    @Override
    public String toString() {
        return "Axis{" +
                "name='" + name + '\'' +
                ", humanReadableName='" + humanReadableName + '\'' +
                '}';
    }
}
