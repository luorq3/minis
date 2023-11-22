package com.minis.beans.factory;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.support.BeanDefinitionRegistry;
import com.minis.beans.factory.support.DefaultSingletonBeanRegistry;
import com.minis.core.PropertyValue;
import com.minis.core.PropertyValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry
        implements BeanFactory, BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private List<String> beanDefinitionNames = new ArrayList<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public AbstractBeanFactory() {}

    @Override
    public void refresh() {
        try {
            for (String beanName : beanDefinitionNames) {
                getBean(beanName);
            }
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                System.out.println("get bean null-------" + beanName);
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                singleton = this.createBean(beanDefinition);
                this.registerSingleton(beanName, singleton);
                // post process
                // step.1: postProcessAfterInitialization
                applyBeanPostProcessBeforeInitialization(singleton, beanName);
                // step.2: init-method
                if (beanDefinition.getInitMethodName() != null && !beanDefinition.getInitMethodName().equals("")) {
                    invokeInitMethod(beanDefinition, singleton);
                }
                // step.3: postProcessAfterInitialization
                applyBeanPostProcessAfterInitialization(singleton, beanName);
            }
        }
        return singleton;
    }

    protected void invokeInitMethod(BeanDefinition beanDefinition, Object obj) {
        Class<?> clazz = obj.getClass();
        Method method;
        try {
            method = clazz.getMethod(beanDefinition.getInitMethodName());
            method.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bd) {
        this.beanDefinitionMap.put(name, bd);
        this.beanDefinitionNames.add(name);
        if (!bd.isLazyInit()) {
            try {
                getBean(bd.getId());
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clazz = null;
        Object obj = doCreateBean(beanDefinition);
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clazz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        populateBean(beanDefinition, clazz, obj);
        return obj;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clazz;
        Object obj = null;
        Constructor<?> con;

        try {
            clazz = Class.forName(beanDefinition.getClassName());
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(constructorArgumentValue.getType()) || "java.lang.String".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    } else if ("Integer".equals(constructorArgumentValue.getType()) || "java.lang.Integer".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else if ("int".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    }
                }
                try {
                    con = clazz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                obj = clazz.getConstructor().newInstance();
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(beanDefinition.getId() + " bean created."
                + beanDefinition.getClassName() + ": " + obj.toString());
        return obj;
    }

    private void populateBean(BeanDefinition beanDefinition, Class<?> clazz, Object obj) {
        handleProperties(beanDefinition, clazz, obj);
    }

    private void handleProperties(BeanDefinition beanDefinition, Class<?> clazz, Object obj) {
        System.out.println("handle properties for bean: " + beanDefinition.getId());
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                Class<?>[] paramTypes = new Class<?>[1];

                Object[] paramValues = new Object[1];
                if (isRef) {
                    try {
                        paramTypes[0] = Class.forName(pType);
                        paramValues[0] = getBean((String) pValue);
                    } catch (ClassNotFoundException | BeansException e) {
                        e.printStackTrace();
                    }
                } else {
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }
                    paramValues[0] = pValue;
                }

                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                Method method = null;
                try {
                    method = clazz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract Object applyBeanPostProcessAfterInitialization(Object singleton, String beanName) throws BeansException;

    protected abstract Object applyBeanPostProcessBeforeInitialization(Object singleton, String beanName) throws BeansException;
}
