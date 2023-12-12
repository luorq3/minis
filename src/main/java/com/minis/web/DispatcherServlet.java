package com.minis.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author luoruiqing
 */
public class DispatcherServlet extends HttpServlet {

    private String sContextConfigLocation;
    private List<String> packageNames;
    private Map<String, Object> controllerObjs = new HashMap<>();
    private List<String> controllerNames;
    private Map<String, Class<?>> controllerClasses = new HashMap<>();
    private final List<String> urlMappingNames = new ArrayList<>();
    private final Map<String, Object> mappingObjs = new HashMap<>();
    private final Map<String, Method> mappingMethods = new HashMap<>();
    private WebApplicationContext webApplicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.webApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        refresh();
    }

    protected void refresh() {
        initController();
        initMapping();
    }

    private void initMapping() {
        for (String controllerName : controllerNames) {
            Class<?> clazz = this.controllerClasses.get(controllerName);
            Object obj = this.controllerObjs.get(controllerName);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                if (isRequestMapping) {
                    String methodName = method.getName();
                    String urlMapping = method.getAnnotation(RequestMapping.class).value();
                    this.urlMappingNames.add(urlMapping);
                    this.mappingObjs.put(urlMapping, obj);
                    this.mappingMethods.put(urlMapping, method);
                }
            }
        }
    }

    private void initController() {
        this.controllerNames = scanPackages(packageNames);
        for (String controllerName : controllerNames) {
            Object obj;
            Class<?> clz = null;
            try {
                clz = Class.forName(controllerName);
                this.controllerClasses.put(controllerName, clz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            }
            try {
                obj = clz.getConstructor().newInstance();
                controllerObjs.put(controllerName, obj);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                System.exit(0);
            }
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sPath = request.getServletPath();
        if (!this.urlMappingNames.contains(sPath)) {
            return;
        }

        Object obj;
        Object objResult = null;
        try {
            Method method = this.mappingMethods.get(sPath);
            obj = this.mappingObjs.get(sPath);
            objResult = method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        response.getWriter().append(Objects.requireNonNull(objResult).toString());
    }
}
