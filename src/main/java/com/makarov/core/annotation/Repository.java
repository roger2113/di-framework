package com.makarov.core.annotation;

import java.lang.annotation.*;

/**
 * Marks interface as bean, which proxy should be registered in ApplicationContext
 * if interface has implementations, only they will be registered
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Repository {

}
