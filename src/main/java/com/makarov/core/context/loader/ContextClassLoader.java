package com.makarov.core.context.loader;

import java.util.List;

public interface ContextClassLoader {

    List<Class> loadJavaClasses(String... packages);

}
