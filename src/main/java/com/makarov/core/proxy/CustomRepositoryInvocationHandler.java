package com.makarov.core.proxy;

import com.makarov.core.annotation.Repository;
import com.makarov.persistence.repository.DefaultDynamicQueryRepository;
import com.makarov.persistence.repository.DynamicQueryRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Class associated with proxy instances for interface,
 * which annotated with {@link Repository}
 * and which not extends any other interfaces from {@link com.makarov.persistence.repository}
 */
public class CustomRepositoryInvocationHandler implements InvocationHandler {

    private static Logger log = Logger.getLogger(CustomRepositoryInvocationHandler.class.getName());

    private DynamicQueryRepository repository = new DefaultDynamicQueryRepository();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("Proxying custom repository method call...");
        return repository.execute(method, args);
    }

}
