package com.makarov.core.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ComponentMethodInterceptor implements MethodInterceptor {

    private static final Logger log = Logger.getLogger(ComponentMethodInterceptor.class.getName());

    private Object target;

    public ComponentMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("CGLib component proxying...");
        return method.invoke(target, args);
    }

}
