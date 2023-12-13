package com.minis.web.method.support;

import com.minis.web.bind.WebDataBinder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luoruiqing
 */
public class WebDataBinderFactory {
    public WebDataBinder createBinder(HttpServletRequest request, Object target, String objectName) {
        WebDataBinder webDataBinder = new WebDataBinder(target, objectName);
        initBinder(webDataBinder, request);
        return webDataBinder;
    }

    private void initBinder(WebDataBinder webDataBinder, HttpServletRequest request) {

    }
}
