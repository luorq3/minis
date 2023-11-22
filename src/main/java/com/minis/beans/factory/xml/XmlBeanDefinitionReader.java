package com.minis.beans.factory.xml;

import com.minis.beans.factory.AbstractBeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.core.*;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    AbstractBeanFactory beanFactory;
    public XmlBeanDefinitionReader(AbstractBeanFactory simpleBeanFactory) {
        this.beanFactory = simpleBeanFactory;
    }

    public void loadBeanFactoryDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

            // properties
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV;
                boolean isRef;
                if (pValue != null && !pValue.equals("")) {
                    isRef = false;
                    pV = pValue;
                } else {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);

            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            // constructor arguments
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                String aValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(aType, aName, aValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);

            this.beanFactory.registerBeanDefinition(beanID, beanDefinition);
        }
    }
}
