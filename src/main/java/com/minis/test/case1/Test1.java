package com.minis.test.case1;

import com.minis.test.case1.entity.AService;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.beans.BeansException;
import com.minis.test.case1.entity.BService;
import com.minis.test.case1.entity.CService;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) ctx.getBean("aservice");
        aService.sayHello();
        aService.act();
        BService bService = (BService) ctx.getBean("bservice");
        bService.act();
        CService cService = (CService) ctx.getBean("cservice");
        cService.act();
    }
}
