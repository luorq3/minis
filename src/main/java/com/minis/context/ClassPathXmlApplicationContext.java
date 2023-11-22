package com.minis.context;

import com.minis.beans.factory.AutowireCapableBeanFactory;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.*;
import com.minis.beans.BeansException;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {

    AutowireCapableBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, false);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanFactoryDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh) {
            this.refresh();
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return beanFactory.getBean(beanName);
    }

    @Override
    public boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public void refresh() {
        registerBeanPostProcessors(beanFactory);
        onRefresh();
    }

    private void onRefresh() {
        this.beanFactory.refresh();
    }

    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    public void publishEvent(ApplicationEvent event) {

    }
}
