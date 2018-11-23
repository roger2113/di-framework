package com.makarov.core.annotation;

import java.lang.annotation.*;

/**
 * Marks class as bean, which should be loaded in ApplicationContext
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

}
