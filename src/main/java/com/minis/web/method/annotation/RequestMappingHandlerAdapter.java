package com.minis.web.method.annotation;

import com.minis.web.WebApplicationContext;
import com.minis.web.method.HandlerMethod;
import com.minis.web.servlet.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author luoruiqing
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    WebApplicationContext wac;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        Method method = handler.getMethod();
        Object obj = handler.getBean();
        Object objResult = null;
        try {
            objResult = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            response.getWriter().append(objResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
