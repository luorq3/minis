package com.minis.beans.factory.annotation;

import com.minis.beans.BeansException;
import com.minis.beans.factory.AutowireCapableBeanFactory;
import com.minis.beans.factory.support.BeanPostProcessor;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private AutowireCapableBeanFactory beanFactory;

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                String fieldName = field.getName();
                Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredObj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
