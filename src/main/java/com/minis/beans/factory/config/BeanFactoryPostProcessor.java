package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessorBeanFactory(BeanFactory beanFactory) throws BeansException;
}
