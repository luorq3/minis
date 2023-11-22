package com.minis.test.entity;

public class BService {
    private CService cService;

    public CService getCService() {
        return cService;
    }

    public void setCService(CService cService) {
        this.cService = cService;
    }

    public void act() {
        System.out.println("this is BService -> CService");
    }
}
