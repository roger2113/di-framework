package com.makarov.persistence.repository;

import com.makarov.persistence.query.MethodSignatureBasedQueryResolver;
import com.makarov.persistence.query.QueryResolver;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultDynamicMethodRepository implements DynamicMethodRepository {

    private static Logger log = Logger.getLogger(DefaultDynamicMethodRepository.class.getName());

    private QueryResolver queryResolver;

    public DefaultDynamicMethodRepository() {
        this.queryResolver = new MethodSignatureBasedQueryResolver();
    }

    public DefaultDynamicMethodRepository(QueryResolver queryResolver) {
        this.queryResolver = queryResolver;
    }

    @Override
    public Object execute(Method method, Object[] args) {
        String query = queryResolver.resolveQuery(method, args);
        log.info("Executing SQL query: " + query);
        Class resultType = method.getReturnType();
        Object result = null;
        try {
            result =  resultType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.log(Level.SEVERE, "Cannot create new instance of type [" + resultType + "]");
        }
        return result;
    }

}
