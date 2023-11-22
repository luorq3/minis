package com.minis.beans.factory;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanDefinitionRegistry;
import com.minis.beans.BeansException;
import com.minis.core.ArgumentValue;
import com.minis.core.ArgumentValues;
import com.minis.core.PropertyValue;
import com.minis.core.PropertyValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(128);
    private List<String> beanDefinitionNames = new ArrayList<>();

    public SimpleBeanFactory() {
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
                singleton = createBean(beanDefinition);
                this.registerSingleton(beanName, singleton);
            }
        }
        return singleton;
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
        handleProperties(beanDefinition, clazz, obj);
        return obj;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clazz;
        Object obj = null;
        Constructor<?> con;

        try {
            clazz = Class.forName(beanDefinition.getClassName());
            ArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
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

    @Override
    public boolean containsBean(String beanName) {
        return containsSingleton(beanName);
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
        return null;
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(name);
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
    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }
}
