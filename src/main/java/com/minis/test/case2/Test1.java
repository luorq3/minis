package com.minis.test.case2;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.test.case2.entity.AService;
import com.minis.test.case2.entity.BService;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("case2.xml");
        AService aService = (AService) ctx.getBean("aservice");
        aService.sayHello();
        aService.act();
        BService bService = (BService) ctx.getBean("bservice");
        bService.act();
    }
}
