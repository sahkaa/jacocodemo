package io.sahka.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class LocalFileHandler extends AbstractFileHandler {

    private static final long serialVersionUID = 2931099634969374050L;

    private transient Path path;

    public LocalFileHandler(Path path) {
        this.path = path;
    }

    public LocalFileHandler(String path) {
        this(Paths.get(path));
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        // If this FileHandler is referring to some exotic filesystem (like, reading from inside a zip file), then we
        // can't safely serialize and deserialize it.
        if (!path.getFileSystem().equals(FileSystems.getDefault())) {
            throw new IOException(
                    "Cannot serialize a LocalFileHandler pointing to a filesystem which is not the default one.");
        }
        out.writeUTF(path.toString());
    }

    private void readObject(ObjectInputStream in) throws IOException {
        path = Paths.get(in.readUTF());
    }

    public File toFile() {
        return this.path.toFile();
    }

    @Override
    public FileHandler child(String... subPath) {
        Path acpath = this.path;
        for (String segment : subPath) {
            if (segment == null) {
                continue;
            }
            segment = segment.replaceAll("\\.[/\\\\]{1}", "");
            if (segment.replaceAll("\\.", "").length() == 0) {
                continue;
            }
            segment = FilenameUtils.separatorsToSystem(segment);
            acpath = acpath.resolve(segment);
        }
        return new LocalFileHandler(acpath);
    }

    @Override
    public boolean exists() {
        return Files.exists(this.path);
    }

    @Override
    public void delete() {
        if (exists() && !FileUtils.deleteQuietly(toFile())) {
            throw new  IllegalStateException("Cannot delete " + this);
        }
    }

    @Override
    public long lastModified() {
        if (!exists()) {
            throw new  IllegalStateException("Cannot check modification timestamp of non existing file : " + this);
        }
        return toFile().lastModified();
    }

    @Override
    public void touch(long lastModified) {
        if (!exists()) {
            try {
                if (!toFile().createNewFile()) {
                    throw new  IllegalStateException("Cannot create new file " + this);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Error creating file " + this, e);
            }
        }
        if (!toFile().setLastModified(lastModified)) {
            throw new  IllegalStateException("Error setting modification timestamp on file " + this);
        }
    }

    @Override
    public Collection<FileHandler> list(String[] extensions) {
        Collection<File> children = FileUtils.listFiles(toFile(), extensions, false);
        return toFileHandler(children);
    }

    private Collection<FileHandler> toFileHandler(Collection<File> children) {
        List<FileHandler> ret = new ArrayList<>();
        for (File child : children) {
            ret.add(new LocalFileHandler(child.toPath()));
        }
        return ret;
    }

    private List<FileHandler> toFileHandlerPath(Collection<Path> children) {
        List<FileHandler> ret = new ArrayList<>();
        for (Path child : children) {
            ret.add(new LocalFileHandler(child));
        }
        return ret;
    }

    @Override
    public Collection<FileHandler> find(final String... globsParam) {
        String[] globs;
        if (globsParam.length == 0) {
            globs = new String[] { "**" };
        } else {
            globs = globsParam;
        }
        final Set<FileHandler> ret = new HashSet<>();

        FileSystem fileSystem = this.path.getFileSystem();
        final List<PathMatcher> matchers = new ArrayList<>(globs.length);
        for (String glob : globs) {
            glob = FilenameUtils.separatorsToSystem(glob);
            matchers.add(fileSystem.getPathMatcher("glob:" + glob));
        }

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path rel = path.relativize(file);
                    for (PathMatcher matcher : matchers) {
                        if (matcher.matches(rel)) {
                            ret.add(new LocalFileHandler(file));
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("Error finding files in " + this, e);
        }
        return ret;
    }

    @Override
    public void mkdir() {
        if (Files.isDirectory(this.path)) {
            return;
        }
        if (!toFile().mkdirs()) {
            throw new  IllegalStateException("Cannot create directories for " + this);
        }
    }

    @Override
    public void write(InputStream cin) {
        try {
            // Even if not stated explicitly, FileUtils.copyInputStreamToFile closes
            // the input stream in a finally block
            FileUtils.copyInputStreamToFile(cin, toFile());
        } catch (IOException e) {
            throw new IllegalStateException("Error writing into " + this, e);
        }
    }

    @Override
    public void setExecutable(boolean b) {
        if (!this.exists() || !toFile().setExecutable(b)) {
            throw new  IllegalStateException("Cannot change executable bit to " + b + " on " + this);
        }
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return Files.newOutputStream(this.path);
        } catch (IOException e) {
            throw new IllegalStateException("Error getting output stream for " + this, e);
        }
    }

    @Override
    public InputStream getInputStream() {
        if (Files.isDirectory(this.path)) {
            throw new  IllegalStateException("Cannot read from directory");
        }
        try {
            return Files.newInputStream(this.path);
        } catch (IOException e) {
            throw new IllegalStateException("Error getting input stream for " + this, e);
        }
    }

    @Override
    public long size() {
        return toFile().length();
    }

    @Override
    public void unzipTo(FileHandler targetPath) {
        try {
            ZipFile zipFile = new ZipFile(toFile());
            zipFile.extractAll(targetPath.toString());
        } catch (ZipException e) {
            throw new IllegalStateException("Error unzipping " + this + " in " + targetPath, e);
        }
    }

    @Override
    public void zip(FileHandler base, String... globs) {
        Collection<FileHandler> files = base.find(globs);
        if (files.isEmpty()) {
            throw new  IllegalStateException("No files found in " + this + " using globs " + Arrays.toString(globs));
        }
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(toFile());
        } catch (ZipException e) {
            throw new IllegalStateException("Error creating zip file " + this, e);
        }
        ZipParameters params = new ZipParameters();

        if (base instanceof LocalFileHandler) {
            params.setDefaultFolderPath(((LocalFileHandler) base).toFile().getAbsolutePath());
        }

        for (FileHandler file : files) {
            try {
                if (file instanceof LocalFileHandler) {
                    zipFile.addFile(((LocalFileHandler) file).toFile(), params);
                } else {
                    ZipParameters streamParams = new ZipParameters();
                    streamParams.setSourceExternalStream(true);
                    streamParams.setFileNameInZip(file.getName());
                    zipFile.addStream(file.getInputStream(), streamParams);
                }
            } catch (ZipException e) {
                throw new IllegalStateException("Error adding " + file + " to zip file " + this, e);
            }
        }
    }

    @Override
    public String toString() {
        return path.toAbsolutePath().toString();
    }

    @Override
    public String getName() {
        return path.getFileName().toString();
    }

    @Override
    public FileHandler getParent() {
        return new LocalFileHandler(path.getParent());
    }

    @Override
    public boolean isDirectory() {
        return Files.isDirectory(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocalFileHandler that = (LocalFileHandler) o;
        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
