package com.minis.beans;

import com.minis.beans.factory.SimpleBeanFactory;
import com.minis.core.Resource;
import org.dom4j.Element;

public class XmlBeanDefinitionReader {
    SimpleBeanFactory simpleBeanFactory;
    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }

    public void loadBeanFactoryDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
//            this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
