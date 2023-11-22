package com.minis.test.case1.entity;

public class CService {
    private AService aService;

    public AService getAService() {
        return aService;
    }

    public void setAService(AService aService) {
        this.aService = aService;
    }

    public void act() {
        System.out.println("this is " + this.getClass().toString() + " ->" + aService.getClass());
    }
}
