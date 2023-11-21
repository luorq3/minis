package com.minis.core;

import java.util.*;

public class ArgumentValues {
    private final Map<Integer, ArgumentValue> indexedArgumentValues = new HashMap<>();
    private final List<ArgumentValue> genericArgumentValues = new LinkedList<>();

    public ArgumentValues() {}
    private void addArgumentValue(Integer key, ArgumentValue newValue) {
        this.indexedArgumentValues.put(key, newValue);
    }
    public boolean hasIndexedArgumentValue(int index) {
        return indexedArgumentValues.containsKey(index);
    }
    public ArgumentValue getIndexedArgumentValue(int index) {
        return indexedArgumentValues.get(index);
    }
    public void addGenericArgumentValue(Object value, String type) {
        this.genericArgumentValues.add(new ArgumentValue(value, type));
    }
    public void addGenericArgumentValue(ArgumentValue newValue) {
        if (newValue.getValues() != null) {
            this.genericArgumentValues.removeIf(curValue -> newValue.getName().equals(curValue.getName()));
        }
        this.genericArgumentValues.add(newValue);
    }
    public ArgumentValue getGenericArgumentValue(String requiredName) {
        for (ArgumentValue valueHolder : this.genericArgumentValues) {
            if (valueHolder.getName() != null && !valueHolder.getName().equals(requiredName)) {
                continue;
            }
            return valueHolder;
        }
        return null;
    }
    public int getArgumentCount() {
        return this.genericArgumentValues.size();
    }
    public boolean isEmpty() {
        return this.genericArgumentValues.isEmpty();
    }
}
