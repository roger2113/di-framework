package com.makarov.core.proxy.cglib;

import com.makarov.persistence.repository.CRUDRepository;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

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
    public static <T> T getRepositoryInterfaceProxy(Class<T> repositoryInterface) {
        MethodInterceptor methodInterceptor = extendsCRUDRepository(repositoryInterface) ?
                new CRUDRepositoryMethodInterceptor() :
                new CustomRepositoryMethodInterceptor();

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{repositoryInterface});
        enhancer.setCallback(methodInterceptor);

        return (T) enhancer.create();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getRepositoryClassProxy(Class<T> repositoryClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(repositoryClass);
        enhancer.setCallback(new ImplementedRepositoryMethodInterceptor());

        return (T) enhancer.create();
    }

    private static boolean extendsCRUDRepository(Class<?> repositoryInterface) {
        return Arrays.stream(repositoryInterface.getInterfaces())
                .anyMatch(intf -> intf.getName().equals(CRUDRepository.class.getName()));
    }

}
