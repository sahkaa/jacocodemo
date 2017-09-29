package io.sahka.files;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public interface FileHandler extends Serializable {

    /**
     * Obtains a child of this file handler.
     *
     * The given subPath is composed of fragments, which are composed into a final
     * path following these rules :
     * <ul>
     * <li>Implicitly, <code>this.toString()</code> is the first fragment</li>
     * <li>Fragments are joined as if calling repeatedly
     * {@link Path#resolve(Path)}</li>
     * <li>If any fragment is absolute (meaning it starts with '/' or '\') previous
     * fragments gets ignores</li>
     * <li>Any '/' or '\' will be considered a path separator.</li>
     * <li>Any null fragment is ignored</li>
     * <li>Paths containing only dots are ignored</li>
     * </ul>
     *
     * @param subPath fragments composing the relative path.
     * @return a descendant FileHandler.
     */
    FileHandler child(String... subPath);

    /**
     * @return the actual string representation of this file handler, which is the complete absolute path.
     */
    @Override
    String toString();

    /**
     * @return a unix-like relative url between this parent file and the given descendant.
     * @throws DFBuildException if the <code>descendant</code> is not an actual descendant of <code>this</code>.
     */
    String relativize(FileHandler descendant);

    /**
     * Checks if this file exists.
     *
     * @return true if the file exists, false otherwise.
     */
    boolean exists();

    /**
     * Checkout if a file is a directory
     *
     * return true if it is a directory. False if the file does not exists or is
     * not a directory
     */
    boolean isDirectory();

    /**
     * Retrieves modification timestamp of this file. If it's referred to a remote
     * file on a remote machine, the timestamp will not be converted to local
     * machine time.
     *
     * @return the modification timestamp of this file.
     */
    long lastModified();

    /**
     * Sets the last modified timestamp. Also creates the a file if it does not
     * exists.
     *
     * @param lastModified the modification timestamp to set.
     */
    void touch(long lastModified);

    /**
     * Deletes this file. If the file was already not existing, this method does
     * nothing.
     */
    void delete();

    /**
     * List child files matching the given extensions.
     *
     * @param extensions extensions to match.
     * @return a collection of child FileHandler.
     */
    Collection<FileHandler> list(String... extensions);

    /**
     * Find all descendant files matching the given glob patterns.
     * <p>
     *     Please note that the pattern in globs is dependent on the system implementation:
     *     <br>
     *     Inside TC it uses FileSystem.getPathMatcher:
     *     https://docs.oracle.com/javase/8/docs/api/java/nio/file/FileSystem.html#getPathMatcher-java.lang.String-
     *     <br>
     *     Inside Jenkins it uses Ant path style. This can lead to differences like in the following example.
     * </p>
     * <p>
     *     Given the following files :
     *     <pre>
     *         /root
     *         /root/file1.txt
     *         /root/subfolder/file2.txt
     *     </pre>
     *     <ul>
     *     <li>Under ant path style ** /*.txt on /root matches both files</li>
     *     <li>Under getPathMatcher above expression only matches txt files on subfolders.</li>
     *     </ul>
     *     Check BaseFileHandlerTest to see how to use it on a portable way
     * </p>
     * @param globs Glob patterns to search for.
     */
    Collection<FileHandler> find(String... globs);

    /**
     * Find multiple files using either given globs, or full paths, or absolute paths, or
     * absolute globs.
     * <p>
     * While the {@link #find(String...)} method only find descending files and expect as its
     * only argument an array of globs or relative paths, this method allows also absolute
     * paths pointing to files not descending this FileHandler or absolute globs.
     * </p>
     * <p>
     * Please note that, even if given absolute or relative paths, only existing files will
     * be returned.
     * </p>
     * <p>
     * For example, if <code>this</code> is referring to <code>/project/folder</code>, then
     * we can call this method with the following parameters :
     * <dl>
     * <dt>Relative path: file.txt</dt>
     * <dd>Will find the specific file <code>/project/folder/file.txt</code></dd>
     * <dt>Relative glob: *.txt</dt>
     * <dd>Will find <code>/project/folder/*.txt</code>, equivalent to {@link #find(String...)}</dd>
     * <dt>Relative ant-style glob: ** /*.txt</dt>
     * <dd>Will find <code>/project/folder/** /*.txt</code>, equivalent to {@link #find(String...)}</dd>
     * <dt>Absolute path: /other/folder/file.txt</dt>
     * <dd>Will find the specific file, even if it's not descendant of <code>this</code> path</dd>
     * <dt>Ansolute (ant-style) glob: /other/folder/*.txt</dt>
     * <dd>Will find *.txt files in the given folder, even if those are not descendant of
     * <code>this</code> path. This is equivalent to calling <code>this.child("/other/folder").find("*.txt")</code>
     * </dd>
     * </dl>
     * </p>
     */
    Collection<FileHandler> findMultiple(String... globsOrPaths);

    /**
     * Creates the folder corresponding to this file handler, and parent folders
     * if needed.
     */
    void mkdir();

    /**
     * Writes the gives string into this file. Calling this method implicitly
     * creates this file and parent folders if needed.
     *
     * @param string content to write into the file.
     */
    void write(String string);

    /**
     * Writes from the given input stream into this file. Calling this method
     * implicitly creates this file and parent folders if needed. The given input
     * stream will be closed by this method, even if an error occurs.
     *
     * @param cin the input stream to read the content from.
     */
    void write(InputStream cin);

    /**
     * Sets the executable bit on this file.
     *
     * @param flag the value to set the executable flag to.
     */
    void setExecutable(boolean flag);

    /**
     * Gets an output stream to write to this file. Calling this method implicitly
     * creates this file and parent folders if needed.
     *
     * @return an output stream to write to this file.
     */
    OutputStream getOutputStream();

    /**
     * Gets and input stream to read from this file.
     *
     * @return an input stream to read from this file.
     */
    InputStream getInputStream();

    /**
     * @return the size of this file.
     */
    long size();

    /**
     * Unzips this file into the given folder.
     *
     * @param testPath folder into which to unzip this file.
     */
    void unzipTo(FileHandler testPath);

    /**
     * Returns the name of the file or directory denoted by this path as a Path
     * object. The file name is the farthest element from the root in the
     * directory hierarchy.
     *
     * @return The name of the file or directory
     */
    String getName();

    /**
     * Returns the parent folder of the specified file/dir
     *
     * @return the parent folder of that file or dir
     */
    FileHandler getParent();

    /**
     * Zips items into this file.
     *
     * @param base the base directory where to find files from
     * @param globs the globs of files to include in the zip file.
     */
    void zip(FileHandler base, String... globs);

}
