package com.makarov;

import com.makarov.core.context.loader.SimpleContextClassLoader;
import com.makarov.core.context.loader.ContextClassLoader;

import java.util.List;

/**
 * Class to test {@link com.makarov.core.context.loader.SimpleContextClassLoader}
 *
 * Cases:
 * 1) 6 java classes from {@link com.makarov.samples1} package
 * 2) 3 - from {@link com.makarov.samples2} package, including 1 in inner package
 * 3) 9 - from both packages
 * 4) 0 - from {@link com.makarov.samples2} package
 */
public class ContextClassLoaderTest {

    public static void main(String[] args) {
        ContextClassLoader classLoader = new SimpleContextClassLoader();

        //CASE 1
        List<Class> classes = classLoader.loadJavaClasses("com.makarov.samples1");
        int classesLoaded = classes.size();
        AssertUtil.assertTrue(classesLoaded == 6, String.format("Java classes loaded not properly, " +
                "6 - needed, %d - actual", classesLoaded));

        //CASE 2
        classes = classLoader.loadJavaClasses("com.makarov.samples2");
        classesLoaded = classes.size();
        AssertUtil.assertTrue(classesLoaded == 3, String.format("Java classes loaded not properly, " +
                "3 - needed, %d - actual", classesLoaded));

        //CASE 3
        classes = classLoader.loadJavaClasses("com.makarov.samples1", "com.makarov.samples2");
        classesLoaded = classes.size();
        AssertUtil.assertTrue(classesLoaded == 9, String.format("Java classes loaded not properly, " +
                "9 - needed, %d - actual", classesLoaded));

        //CASE 4
        classes = classLoader.loadJavaClasses("com.makarov.samples3");
        AssertUtil.assertTrue(classes.isEmpty(), String.format("Java classes loaded not properly, " +
                "0 - needed, %d - actual", classes.size()));

    }

}
