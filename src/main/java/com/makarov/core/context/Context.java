package com.makarov.core.context;

public interface Context {

    void invoke(String... packages);

    <T> Object getBean(Class<T> clazz);

    <T> Object getBeanProxy(Class<T> clazz);

    <T> Object getBean(String name);
}
