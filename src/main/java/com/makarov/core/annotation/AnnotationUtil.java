package com.makarov.core.annotation;

public class AnnotationUtil {

    public static Class[] getBeanAnnotations() {
        return new Class[] {
                Component.class,
                Repository.class
        };
    }

}
