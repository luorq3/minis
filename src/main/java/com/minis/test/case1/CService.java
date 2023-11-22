package com.minis.test.case1.entity;

import com.minis.beans.factory.annotation.Autowired;

public class CService {
    @Autowired
    private com.minis.test.case1.entity.AService aService;

    public com.minis.test.case1.entity.AService getAService() {
        return aService;
    }

    public void setAService(com.minis.test.case1.entity.AService aService) {
        this.aService = aService;
    }

    public void act() {
        System.out.println("this is " + this.getClass().toString() + " ->" + aService.getClass());
    }
}
