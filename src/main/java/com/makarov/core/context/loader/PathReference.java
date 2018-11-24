package com.makarov.core.context.loader;

import java.nio.file.FileSystem;
import java.nio.file.Path;

public class PathReference implements AutoCloseable {

    private Path path;
    private FileSystem fileSystem;

    public PathReference(Path path, FileSystem fileSystem) {
        this.path = path;
        this.fileSystem = fileSystem;
    }

    @Override
    public void close() throws Exception {
        if (this.fileSystem != null)
            this.fileSystem.close();
    }

    public Path getPath() {
        return this.path;
    }

    public FileSystem getFileSystem() {
        return this.fileSystem;
    }
}
