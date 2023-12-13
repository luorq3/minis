package com.minis.web.method.annotation;

import com.minis.beans.BeansException;
import com.minis.web.WebApplicationContext;
import com.minis.web.bind.WebDataBinder;
import com.minis.web.method.HandlerMethod;
import com.minis.web.method.support.WebBinderInitializer;
import com.minis.web.method.support.WebDataBinderFactory;
import com.minis.web.servlet.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author luoruiqing
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    WebApplicationContext wac;
    WebBinderInitializer webBinderInitializer;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
        try {
            this.webBinderInitializer = (WebBinderInitializer) this.wac.getBean("webBinderInitializer");
        } catch (BeansException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
        invokeHandlerMethod(request, response, handler);
    }

    protected void invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];

        int i = 0;
        for (Parameter parameter : methodParameters) {
            Object methodParamObj = parameter.getType().getConstructor().newInstance();
            WebDataBinder webDataBinder = binderFactory.createBinder(request, methodParamObj, parameter.getName());
            webDataBinder.bind(request);
            methodParamObjs[i] = methodParamObj;
            i++;
        }

        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
        response.getWriter().append(returnObj.toString());
    }
}
