package com.makarov.core.proxy.cglib;

import com.makarov.core.annotation.Repository;
import com.makarov.persistence.repository.CRUDRepository;
import com.makarov.persistence.repository.DefaultCRUDRepository;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

/**
 * Class associated with proxy instances for interfaces,
 * which annotated with {@link Repository}
 * and which implements {@link com.makarov.persistence.repository.CRUDRepository}
 *
 * Mainly created for intercepting "findBy...()" method calls
 * to create and log SQL query, based on method name and given arguments
 */
public class CRUDRepositoryMethodInterceptor implements MethodInterceptor {

    private static final Logger log = Logger.getLogger(CRUDRepositoryMethodInterceptor.class.getName());

    private CRUDRepository repository;

    public CRUDRepositoryMethodInterceptor() {
        this.repository = new DefaultCRUDRepository();
    }

    public CRUDRepositoryMethodInterceptor(CRUDRepository repository) {
        this.repository = repository;
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("Proxying CRUD repository method: " + method.getName()
                + " with args: " + Arrays.toString(args));

        String callMethodName = method.getName();
        List<String> implementedMethodNames = Arrays.stream(repository.getClass().getMethods())
                .map(Method::getName)
                .collect(toList());

        return implementedMethodNames.contains(callMethodName) ?
                method.invoke(repository, args) :
                repository.execute(method, args);    }

}
