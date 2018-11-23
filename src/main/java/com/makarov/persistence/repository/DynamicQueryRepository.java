package com.makarov.persistence.repository;

import java.lang.reflect.Method;

public interface DynamicQueryRepository {

    Object execute(Method method, Object[] args);

}
