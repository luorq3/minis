package com.minis.beans.factory;

import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

public class AutowireCapableBeanFactory extends AbstractBeanFactory {

    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    protected Object applyBeanPostProcessAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()) {
            result = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    @Override
    protected Object applyBeanPostProcessBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()) {
            beanPostProcessor.setBeanFactory(this);
            result = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }
}
