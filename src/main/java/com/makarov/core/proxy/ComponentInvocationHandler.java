package com.makarov.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Class associated with proxy instances for classes,
 * which annotated with {@link com.makarov.core.annotation.Component}
 */
public class ComponentInvocationHandler implements InvocationHandler {

    private final Object target;

    public ComponentInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "Proxy passed;" + method.invoke(target, args);
    }
}
