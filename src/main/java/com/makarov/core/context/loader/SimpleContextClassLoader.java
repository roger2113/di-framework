package com.makarov.core.context.loader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
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

                    for (File classFile : collectFiles(getPath(packageRoot))) {
                        String fileName = classFile.getName();
                        String filePath = classFile.getPath();

                        //Define path for packages located inside root package (inner packages)
                        String innerPackagePath;

                        //in case of loading from .jar
                        if (packageRoot.getPath() == null) {
                            String dotFilePath = filePath.replace('/', '.');
                            int innerPackageBeginIndex = dotFilePath.indexOf(packagePath) + packagePath.length();
                            int innerPackageEndIndex = dotFilePath.lastIndexOf(fileName) - 1;
                            innerPackagePath = dotFilePath.substring(innerPackageBeginIndex, innerPackageEndIndex);
                        }
                        //in case of loading from not archived files
                        else {
                            innerPackagePath = filePath
                                    .substring(packageRoot.getPath().length(), filePath.lastIndexOf(fileName) - 1)
                                    .replace('/', '.');
                        }
                        String className = fileName.substring(0, fileName.lastIndexOf("."));
                        javaClasses.add(Class.forName(packagePath + innerPackagePath + "." + className));
                    }
                }
            } catch (IOException | URISyntaxException | ClassNotFoundException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }
        log.info("Java classes loaded: " + javaClasses);
        return javaClasses;
    }

    /**
     * Loading plain files and archived files requires different FileSystem's
     */
    private PathReference getPath(URI resPath) throws IOException {
        try {
            // first try getting a path via existing file systems
            return new PathReference(Paths.get(resPath), null);
        } catch (final FileSystemNotFoundException e) {
             //not directly on file system, so then it's somewhere else (e.g.: JAR)
            Map<String, ?> env = Collections.emptyMap();
            FileSystem fs = FileSystems.newFileSystem(resPath, env);
            return new PathReference(fs.provider().getPath(resPath), fs);
        }
    }


    /**
     * Method to collect all java class files from given URI,
     * including all files in inner directories
     *
     * @param rootPath
     * @return
     */
    private List<File> collectFiles(PathReference rootPath) {
        try {
            List<File> files = Files.walk(rootPath.getPath())
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".class"))
                    .map(this::toFile)
                    .collect(toList());
                rootPath.close();
            return files;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Cannot load files from package " + rootPath +": " +e.getMessage());
        }
        return Collections.emptyList();
    }

    private File toFile(Path path) {
        try {
            return new File(path.toUri());
        } catch (IllegalArgumentException e) {
            return new File(getClass().getResource(path.toString()).getFile());
        }
    }

    private class PathReference implements AutoCloseable {

        Path path;
        FileSystem fileSystem;

        private PathReference(Path path, FileSystem fileSystem) {
            this.path = path;
            this.fileSystem = fileSystem;
        }

        @Override
        public void close() throws Exception {
            if (this.fileSystem != null)
                this.fileSystem.close();
        }

        Path getPath() {
            return this.path;
        }

        FileSystem getFileSystem() {
            return this.fileSystem;
        }
    }
}
