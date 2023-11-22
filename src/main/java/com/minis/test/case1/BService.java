package com.minis.test.case1.entity;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.case1.CService;

public class BService {
    @Autowired
    private com.minis.test.case1.entity.CService cService;

    public com.minis.test.case1.entity.CService getCService() {
        return cService;
    }

    public void setCService(CService cService) {
        this.cService = cService;
    }

    public void act() {
        System.out.println("this is " + this.getClass().toString() + " ->" + cService.getClass());
    }
}
