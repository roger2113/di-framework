package com.makarov.modules.persistence;

import java.lang.reflect.Method;

public interface QueryBuilder {

    String resolveQuery(Method method, Object[] args);
}
