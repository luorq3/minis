package com.minis.context;

public class ContextRefreshEvent extends ApplicationEvent{
    public ContextRefreshEvent(Object arg0) {
        super(arg0);
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
