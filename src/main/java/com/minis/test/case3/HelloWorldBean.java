package com.minis.test.case3;

import com.minis.web.RequestMapping;

/**
 * @author luoruiqing
 */
public class HelloWorldBean {

    @RequestMapping("/test")
    public String doGet() {
        return "hello world";
    }

    public String doPost() {
        return "hello world";
    }
}
