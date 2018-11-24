package com.makarov.core.context;

import com.makarov.common.exception.ContextInvocationException;

/**
 * Class represents application context instantiating
 * and beans access
 */
public class ContextHandler {

    private static Context context;

    private ContextHandler() {
    }

    public static void invoke(String... packages) {
        if (context != null) {
            throw new ContextInvocationException("Context already instantiated");
        }
        context = new ApplicationContext(packages);
    }

    public static Object getBean(String name) {
        validateContext();
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        validateContext();
        return context.getBean(clazz);
    }

    public static <T> T getBeanProxy(Class<T> clazz) {
        validateContext();
        return context.getBeanProxy(clazz);
    }

    private static void validateContext() {
        if(context == null)
            throw new ContextInvocationException("Context not yet instantiated");
    }

}
