package com.sbutterfly.differential;

import com.sbutterfly.core.Customable;

public class Index {

    private final int paramsIndex;
    private Customable customable;

    public Index(int paramsIndex) {
        if (paramsIndex < 0) {
            throw new NumberFormatException("\"paramsIndex\" must be not negative.");
        }

        this.paramsIndex = paramsIndex;
        this.customable = null;
    }

    public Index(int customIndex, Customable customable) {
        if (customIndex < 0) {
            throw new NumberFormatException("\"paramsIndex\" must be not negative.");
        }
        if (customable == null) {
            throw new NullPointerException("customable");
        }

        this.paramsIndex = customIndex;
        this.customable = customable;
    }

    public boolean isCustom() {
        return customable != null;
    }

    public int getIndex() {
        return paramsIndex;
    }

    public Customable getCustomable() {
        return customable;
    }
}
