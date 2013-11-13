/*
 * Resources.java
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/**
 * A utility class for manipulating resources found inside and outside of JAR
 * files. This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 13 Feb 2013
 * @version 1.0.0.1, 04 June 2013
 * @since 1.0.0.0
 */
public class ResourceUtilities
{
    /**
     * The approximate size of a disk block in bytes. This number is intended to
     * be nearly optimal for copying files across multiple platforms. It may be
     * advisable to compute this constant for each device being copied to if
     * such a method should become readily available.
     *
     * @since 1.0.0.0
     */
    private static final int DISK_BLOCK = 8192;

    /**
     * A private constructor to prevent instantiation.
     *
     * @since 1.0.0.0
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
     * @since 1.0.0.0
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
     * Copies a resource to the specified file on the file system. This method
     * can be useful for the initial setup of a program, when defaults must be
     * copied to the home directory.
     *
     * @param resourceName the name of the resource to copy.
     * @param file the file to copy the resource to. If this represents an
     * existing file, it will be overwritten.
     * @throws IOException if the file can not be created or the resource can
     * not be copied into it.
     * @since 1.0.0.1
     */
    @SuppressWarnings("NestedAssignment")
    public static void copyResourceToFile(String resourceName, File file) throws IOException
    {
        byte[] buffer = new byte[DISK_BLOCK];
        try (InputStream input = ClassLoader.getSystemResourceAsStream(resourceName);
             OutputStream output = new FileOutputStream(file)) {
            int nRead;
            while((nRead = input.read(buffer)) != -1)
                output.write(buffer, 0, nRead);
        }
    }

    /**
     * Loads an image from a resource. To construct an {@code Icon} from the
     * image, use the {@code ImageIcon} class.
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
}
