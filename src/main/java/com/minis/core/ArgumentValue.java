package com.minis.core;

public class ArgumentValue {
    private Object values;
    private String type;
    private String name;

    public ArgumentValue(Object values, String type) {
        this.values = values;
        this.type = type;
    }

    public ArgumentValue(Object values, String type, String name) {
        this.values = values;
        this.type = type;
        this.name = name;
    }

    public void setValues(Object values) {
        this.values = values;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValues() {
        return values;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
