package com.makarov.persistence.query;

import java.lang.reflect.Method;

public interface QueryResolver {

    String resolveQuery(Method method, Object[] args);

}
