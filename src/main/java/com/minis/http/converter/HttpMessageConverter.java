package com.minis.http.converter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author luoruiqing
 */
public interface HttpMessageConverter {
    void write(Object obj, HttpServletResponse response) throws Exception;
}
