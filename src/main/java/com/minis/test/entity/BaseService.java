package com.minis.test.entity;

public class BaseService {
    private BaseBaseService base;

    public BaseBaseService getBase() {
        return base;
    }

    public void setBase(BaseBaseService base) {
        this.base = base;
    }

    public void act() {
        System.out.println("this is BaseService -> BaseBaseService");
    }
}
