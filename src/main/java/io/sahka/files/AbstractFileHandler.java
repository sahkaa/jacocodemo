package io.sahka.files;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 * Abstract base class for {@link FileHandler} class hierarchy.
 * <p>
 * Note: Java 8 interface default methods could be used, but this code base
 * is still sticking to Java 7 for backward compatibility.
 * </p>
 */
public abstract class AbstractFileHandler implements FileHandler {

    @Override
    public Collection<FileHandler> findMultiple(String... globsOrPaths) {
        List<FileHandler> ret = new LinkedList<>();
        List<String> myglobs = new LinkedList<>();
        for (String globOrPath : globsOrPaths) {
            int starPos = globOrPath.indexOf('*');
            int questionPos = globOrPath.indexOf('?');
            if (starPos != -1 || questionPos != -1) {
                // This is a glob
                if (FilenameUtils.getPrefixLength(globOrPath) == 0) {
                    // It is relative, will resolve them later
                    myglobs.add(globOrPath);
                } else {
                    int minPos = Integer.MAX_VALUE;
                    if (starPos != -1) {
                        minPos = Math.min(minPos, starPos);
                    }
                    if (questionPos != -1) {
                        minPos = Math.min(minPos, questionPos);
                    }
                    String absolutePrefix = globOrPath.substring(0, minPos);
                    String globPart = globOrPath.substring(minPos);
                    String namePart = FilenameUtils.getName(absolutePrefix);
                    if (!namePart.isEmpty()) {
                        absolutePrefix = absolutePrefix.substring(0, absolutePrefix.length() - namePart.length());
                        globPart = namePart + globPart;
                    }
                    ret.addAll(child(absolutePrefix).find(globPart));
                }
            } else {
                // It is a path, .child will take care of resolving relative or absolute
                FileHandler child = child(globOrPath);
                if (child.exists()) {
                    ret.add(child);
                }
            }
        }
        if (!myglobs.isEmpty()) {
            ret.addAll(find(myglobs.toArray(new String[myglobs.size()])));
        }
        return ret;
    }

    public String relativize(FileHandler descendant) {
        String childName = descendant.toString();
        String parentName = toString();
        if (!childName.startsWith(parentName)) {
            throw new IllegalStateException("Given file '" + descendant + "' is not a descendant of '" + this + "'");
        }
        if (parentName.equals(childName)) {
            return "./";
        }
        return "." + FilenameUtils.separatorsToUnix(childName.substring(parentName.length()));
    }

    @Override
    public void write(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        write(bais);
    }
}
