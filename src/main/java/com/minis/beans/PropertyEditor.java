package com.minis.beans;

/**
 * @author luoruiqing
 */
public interface PropertyEditor {
    void setAsText(String text);
    void setValue(Object value);
    Object getValue();
    Object getAsText();
}
