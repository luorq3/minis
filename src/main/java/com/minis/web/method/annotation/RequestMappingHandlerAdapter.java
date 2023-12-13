package com.minis.web.method.annotation;

import com.minis.beans.BeansException;
import com.minis.http.converter.DefaultHttpMessageConverter;
import com.minis.http.converter.HttpMessageConverter;
import com.minis.web.WebApplicationContext;
import com.minis.web.bind.WebDataBinder;
import com.minis.web.bind.annotation.ResponseBody;
import com.minis.web.method.HandlerMethod;
import com.minis.web.method.support.WebBinderInitializer;
import com.minis.web.method.support.WebDataBinderFactory;
import com.minis.web.servlet.HandlerAdapter;
import com.minis.web.servlet.ModelAndView;

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
    HttpMessageConverter messageConverter;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
        try {
            this.webBinderInitializer = (WebBinderInitializer) this.wac.getBean("webBinderInitializer");
            this.messageConverter = new DefaultHttpMessageConverter();
        } catch (BeansException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
        return invokeHandlerMethod(request, response, handler);
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
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
        ModelAndView mav = null;
        //如果是ResponseBody注解，仅仅返回值，则转换数据格式后直接写到response
        if (invocableMethod.isAnnotationPresent(ResponseBody.class)) {
            this.messageConverter.write(returnObj, response);
        } else {
            if (returnObj instanceof ModelAndView) {
                mav = (ModelAndView) returnObj;
            } else if (returnObj instanceof String sTarget) {
                mav = new ModelAndView();
                mav.setViewName(sTarget);
            }
        }

        return mav;
    }
}
