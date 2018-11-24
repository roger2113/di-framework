package com.makarov.core.proxy;

import com.makarov.persistence.repository.CRUDRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class CGLibProxyFactory {

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
        Class[] repositoryInterfaces = new Class[]{repositoryInterface};
        InvocationHandler invocationHandler;


        if (targetRepository != null) {
            invocationHandler = new ImplementedRepositoryInvocationHandler(targetRepository);
        } else {
            invocationHandler = extendsCRUDRepository(repositoryInterface) ?
                    new CRUDRepositoryInvocationHandler() :
                    new CustomRepositoryInvocationHandler();
        }

        return (T) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                repositoryInterfaces,
                invocationHandler
        );
    }

    private static boolean extendsCRUDRepository(Class<?> repositoryInterface) {
        return Arrays.stream(repositoryInterface.getInterfaces())
                .anyMatch(intf -> intf.getName().equals(CRUDRepository.class.getName()));
    }

}
