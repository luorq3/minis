package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_NAME = 1;
    int AUTOWIRE_TYPE = 2;

    Object applyBeanPostProcessAfterInitialization(Object existingBean, String beanName) throws BeansException;

    Object applyBeanPostProcessBeforeInitialization(Object existingBean, String beanName) throws BeansException;
}
