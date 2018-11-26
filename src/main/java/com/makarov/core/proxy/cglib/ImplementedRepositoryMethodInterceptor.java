package com.makarov.core.proxy.cglib;

import com.makarov.core.annotation.Repository;
import com.makarov.persistence.repository.DefaultDynamicQueryRepository;
import com.makarov.persistence.repository.DynamicQueryRepository;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Class associated with proxy instances for interface,
 * which annotated with {@link Repository}
 * and has user provided implementation
 */
public class ImplementedRepositoryMethodInterceptor implements MethodInterceptor {

    private static final Logger log = Logger.getLogger(ImplementedRepositoryMethodInterceptor.class.getName());

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("Proxying implemented repository method: " + method.getName()
                + " with args: " + Arrays.toString(args));

        return methodProxy.invokeSuper(target, args);
    }

}
