package com.makarov.core.context;

public interface Context {

    <T> T getBean(Class<T> clazz);

    <T> T getBeanProxy(Class<T> clazz);

    <T> T getBean(String name);
}
