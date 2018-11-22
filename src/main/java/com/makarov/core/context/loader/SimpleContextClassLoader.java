package com.makarov.core.context.loader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

/**
 * Class responsible for loading Java classes from given packages
 */
public class SimpleContextClassLoader implements ContextClassLoader {

    private static Logger log = Logger.getLogger(SimpleContextClassLoader.class.getName());

    @Override
    public List<Class> loadJavaClasses(String... packages) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        List<Class> javaClasses = new ArrayList<>();

        for (String packagePath : packages) {
            try {
                log.info("Loading java classes from package: " + packagePath);
                String path = packagePath.replace('.', '/');
                Enumeration<URL> resources = classLoader.getResources(path);

                while (resources.hasMoreElements()) {
                    URI packageRoot = resources.nextElement().toURI();

                    for (File classFile : collectFiles(packageRoot)) {
                        String fileName = classFile.getName();
                        String filePath = classFile.getPath();

                        //Define path for packages located inside root package (inner packages)
                        String innerPackagePath = filePath.substring(packageRoot.getPath().length(), filePath.lastIndexOf(fileName) - 1)
                                .replace('/', '.');
                        String className = fileName.substring(0, fileName.lastIndexOf("."));
                        javaClasses.add(Class.forName(packagePath + innerPackagePath + "." + className));
                    }
                }
            } catch (IOException | URISyntaxException | ClassNotFoundException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }
        return javaClasses;
    }


    /**
     * Method to collect all java class files from given URI,
     * including all files in inner directories
     *
     * @param rootPath
     * @return
     */
    private List<File> collectFiles(URI rootPath) {
        try {
            return Files.walk(Paths.get(rootPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".class"))
                    .map(path -> new File(path.toUri()))
                    .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
