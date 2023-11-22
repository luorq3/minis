package com.minis.test.entity;

public class BaseBaseService {
    private AService as;

    public AService getAs() {
        return as;
    }

    public void setAs(AService as) {
        this.as = as;
    }

    public void act() {
        System.out.println("this is BaseBaseService -> AService");
    }
}
