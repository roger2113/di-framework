package com.makarov.persistence.repository;

import com.makarov.core.annotation.Component;
import com.makarov.persistence.query.MethodSignatureBasedQueryResolver;
import com.makarov.persistence.query.QueryResolver;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DefaultDynamicQueryRepository implements DynamicQueryRepository {

    private static Logger log = Logger.getLogger(DefaultDynamicQueryRepository.class.getName());

    private QueryResolver queryResolver;

    public DefaultDynamicQueryRepository() {
        this.queryResolver = new MethodSignatureBasedQueryResolver();
    }

    public DefaultDynamicQueryRepository(QueryResolver queryResolver) {
        this.queryResolver = queryResolver;
    }

    @Override
    public Object execute(Method method, Object[] args) {
        String query = queryResolver.resolveQuery(method, args);
        log.log(Level.INFO, "Executing SQL query: {0}\n", query);
        Class resultType = method.getReturnType();
        Object result = null;
        try {
            result =  resultType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.log(Level.SEVERE, "Cannot create new instance of type [{0}]",  resultType );
        }
        return result;
    }

}
