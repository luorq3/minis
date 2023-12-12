package com.minis.web.context;

import com.minis.context.ApplicationContext;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.web.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author luoruiqing
 */
public class ContextLoaderListener implements ServletContextListener {
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    private WebApplicationContext context;

    public ContextLoaderListener() {
    }

    public ContextLoaderListener(WebApplicationContext context) {
        this.context = context;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    private void initWebApplicationContext(ServletContext servletContext) {
        String sContextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        ApplicationContext wac = new ClassPathXmlApplicationContext(sContextLocation);

    }
}
