package com.minis.web;

import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.*;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author luoruiqing
 */
public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {

    private WebApplicationContext parentApplicationContext;

    private ServletContext servletContext;

    DefaultListableBeanFactory beanFactory;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public AnnotationConfigWebApplicationContext(String fileName) {
        this(fileName, null);
    }

    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        this.servletContext = this.parentApplicationContext.getServletContext();
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        List<String> controllerNames = scanPackages(packageNames);
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        this.beanFactory = bf;
        this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
        loadBeanDefinitions(controllerNames);

        if (true) {
            try {
                refresh();
            } catch (BeansException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    private void loadBeanDefinitions(List<String> controllerNames) {
        for (String controller : controllerNames) {
            String beanId = controller;
            String beanClassName = controller;
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            this.beanFactory.registerBeanDefinition(beanId, beanDefinition);
        }
    }

    private List<String> scanPackages(List<String> packageNames) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packageNames) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URI uri = null;
        try {
            uri = Objects.requireNonNull(this.getClass().getResource("/" + packageName.replaceAll("\\.", "/"))).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(0);
        }
        File dir = new File(uri);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanPackage(packageName + "." + file.getName());
            } else {
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    public void setParent(WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    @Override
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    protected void initApplicationEventPublisher() {
        ApplicationEventPublisher publisher = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(publisher);
    }

    @Override
    protected void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    protected void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    protected void finishRefresh() {

    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object bean = super.getBean(beanName);
        if (bean == null) {
            bean = this.parentApplicationContext.getBean(beanName);
        }
        return bean;
    }
}
