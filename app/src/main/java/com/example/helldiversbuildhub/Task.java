package com.example.helldiversbuildhub;

import java.util.List;

public class Task {
    private int type;
    private List<Integer> values;
    private List<Integer> valueTypes;

    public Task() { }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public List<Integer> getValueTypes() {
        return valueTypes;
    }

    public void setValueTypes(List<Integer> valueTypes) {
        this.valueTypes = valueTypes;
    }
}