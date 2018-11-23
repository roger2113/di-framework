package com.makarov.persistence.repository;

import java.lang.reflect.Method;

public interface DynamicMethodRepository {

    Object execute(Method method, Object[] args);

}
