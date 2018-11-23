package com.makarov.core.proxy;

import com.makarov.core.annotation.Repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Class associated with proxy instances for interface,
 * which annotated with {@link Repository}
 * and has user provided implementation
 */
public class ImplementedRepositoryInvocationHandler implements InvocationHandler {

    private static Logger log = Logger.getLogger(ImplementedRepositoryInvocationHandler.class.getName());

    private Object target;

    public ImplementedRepositoryInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        log.info("Proxying implemented repository method call...");
        return method.invoke(target, args);
    }

}
