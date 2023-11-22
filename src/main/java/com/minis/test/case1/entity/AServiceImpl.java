package com.minis.test.case1.entity;

public class AServiceImpl implements AService {
    private String name;
    private int level;
    private String property1;
    private String property2;
    private BService ref1;

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

    public void setRef1(BService ref1) {
        this.ref1 = ref1;
    }

    @Override
    public void sayHello() {
        System.out.println(this.property1 + ", " + this.property2);
    }

    @Override
    public void act() {
        System.out.println("this is " + this.getClass().toString() + " ->" + ref1.getClass());
    }
}
