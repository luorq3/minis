package com.minis.web.bind;

import com.minis.beans.PropertyEditor;
import com.minis.beans.PropertyEditorRegistrySupport;
import com.minis.core.PropertyValue;
import com.minis.core.PropertyValues;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author luoruiqing
 */
public class BeanWrapperImpl extends PropertyEditorRegistrySupport {
    Object wrappedObject;
    Class<?> clz;
    PropertyValues pvs;

    public BeanWrapperImpl(Object target) throws Exception {
        super();
        registerDefaultEditors();
        this.wrappedObject = target;
        this.clz = target.getClass();
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
    }

    public Object getBeanInstance() {
        return this.wrappedObject;
    }

    public void setPropertyValues(PropertyValues mpvs) {
        this.pvs = mpvs;
        for (PropertyValue pv : mpvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    private void setPropertyValue(PropertyValue pv) {
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
        if (pe == null) {
            pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        }
        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());
    }

    class BeanPropertyHandler {
        Method writeMethod = null;
        Method readMethod = null;
        Class<?> propertyClz = null;

        public BeanPropertyHandler(String propertyName) {
            try {
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();
                this.writeMethod = clz.getDeclaredMethod("set" +
                        propertyName.substring(0, 1).toUpperCase() +
                        propertyName.substring(1), propertyClz);
                this.readMethod = clz.getDeclaredMethod("get" +
                        propertyName.substring(0, 1).toUpperCase() +
                        propertyName.substring(1), propertyClz);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        public Object getValue() {
            Object result = null;
            readMethod.setAccessible(true);
            try {
                result = readMethod.invoke(wrappedObject);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
            return result;
        }

        public Class<?> getPropertyClz() {
            return propertyClz;
        }

        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
