package com.minis.context;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.core.*;
import com.minis.beans.BeansException;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {

    BeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, false);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        SimpleBeanFactory beanFactory = new SimpleBeanFactory();
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
        this.beanFactory.refresh();
    }


    @Override
    public void publishEvent(ApplicationEvent event) {

    }
}
