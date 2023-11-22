package com.minis.beans.factory.config;

import java.util.*;

public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> constructorArgumentValueList = new LinkedList<>();

    public ConstructorArgumentValues() {}

    public void addArgumentValue(ConstructorArgumentValue newValue) {
        this.constructorArgumentValueList.add(newValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return constructorArgumentValueList.get(index);
    }

    public int getArgumentCount() {
        return this.constructorArgumentValueList.size();
    }

    public boolean isEmpty() {
        return this.constructorArgumentValueList.isEmpty();
    }
}
