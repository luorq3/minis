package com.minis.test.case1.entity;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.case1.BService;

public class AServiceImpl implements com.minis.test.case1.entity.AService {
    private String name;
    private int level;
    private String property1;
    private String property2;
    @Autowired
    private com.minis.test.case1.entity.BService ref1;

    public AServiceImpl() {}

    public AServiceImpl(String name, Integer level) {
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public BService getRef1() {
        return ref1;
    }

    public void setRef1(com.minis.test.case1.entity.BService ref1) {
        this.ref1 = ref1;
    }

    @Override
    public void sayHello() {
        System.out.println(this.property1 + ", " + this.property2);
    }

    public void act() {
        System.out.println("this is " + this.getClass().toString() + " ->" + ref1.getClass());
    }
}
