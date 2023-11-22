package com.minis.test;

import com.minis.test.entity.AService;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.beans.BeansException;
import com.minis.test.entity.BaseBaseService;
import com.minis.test.entity.BaseService;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) ctx.getBean("aservice");
        aService.sayHello();
        aService.act();
        BaseService baseService = (BaseService) ctx.getBean("baseservice");
        baseService.act();
        BaseBaseService baseBaseService = (BaseBaseService) ctx.getBean("basebaseservice");
        baseBaseService.act();
    }
}
