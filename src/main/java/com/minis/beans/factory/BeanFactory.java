package com.minis.beans.factory;

import com.minis.beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    boolean containsBean(String beanName);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);
    void refresh();
}
