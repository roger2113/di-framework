package com.makarov.core.proxy;

import com.makarov.core.annotation.Repository;
import com.makarov.core.context.ContextHandler;
import com.makarov.persistence.repository.CRUDRepository;
import com.makarov.persistence.repository.DefaultCRUDRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Class associated with proxy instances for interfaces,
 * which annotated with {@link Repository}
 * and which implements {@link com.makarov.persistence.repository.CRUDRepository}
 *
 * Mainly created for intercepting "findBy...()" method calls
 * to create and log SQL query, based on method name and given arguments
 */
public class CRUDRepositoryInvocationHandler implements InvocationHandler {

    private static Logger log = Logger.getLogger(CRUDRepositoryInvocationHandler.class.getName());

    private CRUDRepository repository;

    public CRUDRepositoryInvocationHandler() {
        this.repository = new DefaultCRUDRepository();
    }

    public CRUDRepositoryInvocationHandler(CRUDRepository repository) {
        this.repository = repository;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Proxying CRUD repository method call...");

        String callMethodName = method.getName();
        List<String> implementedMethodNames = Arrays.stream(repository.getClass().getMethods())
                .map(Method::getName)
                .collect(toList());

        return implementedMethodNames.contains(callMethodName) ?
                method.invoke(repository, args) :
                repository.execute(method, args);
    }

}
