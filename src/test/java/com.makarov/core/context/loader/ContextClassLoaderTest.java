package com.makarov.core.context.loader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(BlockJUnit4ClassRunner.class)
public class ContextClassLoaderTest {

    private static SimpleContextClassLoader classLoader;
    private static final String ROOT = "com.makarov.samples.contextClassLoaderSamples.";

    @BeforeClass
    public static void init() {
        classLoader = new SimpleContextClassLoader();
    }

    @Test
    public void loadClasses() {
        List<Class> classes = loadFrom(ROOT + "samples1");
        assertEquals(3, classes.size());
    }

    @Test
    public void loadClasses_IncludingClassesFromInnerPackage() {
        List<Class> classes = loadFrom(ROOT + "samples2");
        assertEquals(2, classes.size());
    }

    @Test
    public void loadNineClasses_FromMultiplePackages() {
        List<Class> classes = loadFrom(ROOT + "samples1", ROOT + "samples2");
        assertEquals(5, classes.size());
    }

    @Test
    public void loadZeroClasses_FromPackageWithoutJavaFiles() {
        List<Class> classes = loadFrom(ROOT + "samples3");
        assertTrue(classes.isEmpty());
    }

    private List<Class> loadFrom(String... packages) {
        return classLoader.loadJavaClasses(packages);
    }

}
