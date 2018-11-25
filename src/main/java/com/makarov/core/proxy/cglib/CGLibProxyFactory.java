package com.makarov.core.proxy.cglib;

import com.makarov.core.proxy.jdk.CRUDRepositoryInvocationHandler;
import com.makarov.core.proxy.jdk.CustomRepositoryInvocationHandler;
import com.makarov.core.proxy.jdk.ImplementedRepositoryInvocationHandler;
import com.makarov.persistence.repository.CRUDRepository;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class CGLibProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getComponentProxy(Class<T> clazz, T object) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new ComponentMethodInterceptor(object));
        return (T) enhancer.create();
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
