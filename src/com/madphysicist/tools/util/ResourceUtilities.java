/*
 * ResourceUtilities.java
 *
 * Mad Physicist JTools Project (General Purpose Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 by Joseph Fox-Rabinovitz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.madphysicist.tools.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A utility class for manipulating resources found inside and outside of JAR
 * files. This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 13 Feb 2013 - J. Fox-Rabinovitz - Created
 * @version 1.0.1, 04 Jun 2013 - J. Fox-Rabinovitz - Added copyResourceToFile()
 * @version 1.0.2, 16 Nov 2013 - J. Fox-Rabinovitz - Added loadLibrary() and loadIcon()
 * @since 1.0.0.
 */
public class ResourceUtilities
{
    /**
     * A private constructor to prevent instantiation.
     *
     * @since 1.0.0
     */
    private ResourceUtilities() {}

    /**
     * Copies a named resource into a temporary file on the file system. An
     * object referencing the newly created file is returned. This method is
     * useful when random access or marking within the resource stream are
     * required. In such a case (as with audio files), obtaining the resource as
     * a stream from the default class loader is not sufficient.
     *
     * @param resourceName the name of the resource to copy.
     * @param deleteOnExit specifies whether or not the file should be deleted
     * when the application terminates.
     * @return a {@code File} referfencing the newly created copy of the
     * resource.
     * @throws IOException if the temporary file can not be created or the
     * resource can not be copied into it.
     * @since 1.0.0
     */
    public static File getResourceAsFile(String resourceName, boolean deleteOnExit) throws IOException
    {
        int extensionIndex = resourceName.lastIndexOf('.');
        String name = resourceName.replace('/', '.').replace('\\', '.');
        File file = (extensionIndex == -1) ?
                    File.createTempFile(name, null) :
                    File.createTempFile(name.substring(0, extensionIndex),
                                        name.substring(extensionIndex));
        if(deleteOnExit)
            file.deleteOnExit();

        copyResourceToFile(resourceName, file);
        return file;
    }

    /**
     * Extracts a native dynamically linked library from a resource and loads it
     * into memory. This method is particularly useful for loading .so and .dll
     * modules stored within a JAR file.
     *
     * @param parent the root path of the resource, minus the file name.
     * @param baseName the base name of the library. Any extensions will be
     * interpreted by the loacl platform as necessary.
     * @throws IOException if the library can not be extracted for any reason.
     * @throws UnsatisfiedLinkError if the library can not be linked after it is
     * extracted.
     * @see System#mapLibraryName(java.lang.String)
     * @since 1.0.2
     */
    public static void loadLibrary(String parent, String baseName) throws IOException, UnsatisfiedLinkError
    {
        File lib = getResourceAsFile(baseName + File.separator + System.mapLibraryName(baseName), true);
        System.load(lib.getCanonicalPath());
    }

    /**
     * Copies a resource to the specified file on the file system. This method
     * can be useful for the initial setup of a program, when defaults must be
     * copied to the home directory.
     *
     * @param resourceName the name of the resource to copy.
     * @param file the file to copy the resource to. If this represents an
     * existing file, it will be overwritten.
     * @throws IOException if the file can not be created or the resource can
     * not be copied into it.
     * @since 1.0.1
     */
    public static void copyResourceToFile(String resourceName, File file) throws IOException
    {
        try (InputStream input = ClassLoader.getSystemResourceAsStream(resourceName)) {
            FileUtilities.copyStreamToFile(input, file);
        }
    }

    /**
     * Loads an image from a resource. To load the image as an {@code Icon}, use
     * the {@link #loadIcon(java.lang.String) loadIcon()} method.
     *
     * @param resourceName the name of the resource to load.
     * @return the image named by the specified resource, or {@code null} if the
     * resource could not be found.
     * @throws IOException if an error occurs while opening or reading the
     * resource stream.
     * @since 1.0.0
     */
    public static Image loadImage(String resourceName) throws IOException
    {
        try(InputStream input = ClassLoader.getSystemResourceAsStream(resourceName)) {
            return (input == null) ? null : ImageIO.read(input);
        }
    }

    /**
     * Loads an icon from a resource. This method wraps an image returned by
     * {@link #loadImage(java.lang.String) loadImage()} into an {@code Icon}.
     * It also returns {@code null} instead of throwing an exception.
     *
     * @param resourceName the name of the resource to load.
     * @return the icon named by the specified resource, or {@code null} if the
     * resource could not be found or loaded.
     * @since 1.0.2
     */
    public static Icon loadIcon(String resourceName)
    {
        Image image = null;
        try {
            image = loadImage(resourceName);
        } catch(IOException ioe) {}
        if(image != null) {
            return new ImageIcon(image);
        }
        return null;
    }
}
