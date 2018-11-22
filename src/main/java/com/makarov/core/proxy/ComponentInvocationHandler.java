package com.makarov.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ComponentInvocationHandler implements InvocationHandler {

    private Object target;

    public ComponentInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "Proxy passed;" + method.invoke(target, args);
    }
}
