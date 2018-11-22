package com.makarov.core.proxy;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getComponentProxy(Class<T> clazz, T object) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                object.getClass().getInterfaces(),
                new ComponentInvocationHandler(object)
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> T getRepositoryProxy(Class<T> repositoryInterface, T targetRepository) {
        Class[] interfaces = targetRepository != null ?
                targetRepository.getClass().getInterfaces() :
                new Class[] {repositoryInterface};

        return (T) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                interfaces,
                new RepositoryInvocationHandler(targetRepository)
        );
    }
}
