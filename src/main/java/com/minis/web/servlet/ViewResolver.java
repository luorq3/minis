package com.minis.web.servlet;

/**
 * @author luoruiqing
 */
public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
