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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
 * @version 1.1.0, 20 Jun 2014 - J. Fox-Rabinovitz - Added support for cursors, updated getResourceAsFile(),
 *                                                   loadFullImage() methods, and added PREFER_TOOLKIT flag.
 * @since 1.0.0
 */
public class ResourceUtilities
{
    /**
     * Determines whether image loading methods should use {@code javax.imageio.ImageIO.read()} or
     * {@code java.awt.Toolkit}.
     *
     * @see javax.imageio.ImageIO#read(File)
     * @see javax.imageio.ImageIO#read(URL)
     * @see java.awt.Toolkit#createImage(String)
     * @see java.awt.Toolkit#createImage(URL)
     * @since 1.1.0
     */
    private static final boolean PREFER_TOOLKIT = false;

    /**
     * An enum specifying the location of a cursor hot-spot coordinates relative to the size of the cursor. 
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 20 Jun 2014 - J. Fox-Rabinovitz - Created
     * @since 1.1.0
     */
    public static enum CursorHotspot
    {
        CURSOR_TOP_LEFT(0.0, 0.0),
        CURSOR_TOP(0.5, 0.0),
        CURSOR_TOP_RIGHT(1.0, 0.0),
        CURSOR_LEFT(0.0, 0.5),
        CURSOR_CENTER(0.5, 0.5),
        CURSOR_RIGHT(1.0, 0.5),
        CURSOR_BOTTOM_LEFT(0.0, 1.0),
        CURSOR_BOTTOM(0.5, 1.0),
        CURSOR_BOTTOM_RIGHT(1.0, 1.0);

        /**
         * The x-coordinate of the hot-spot, ranging from {@code 0.0} to {@code 1.0} (inclusive).
         *
         * @since 1.0.0
         */
        private final double x;

        /**
         * The x-coordinate of the hot-spot, ranging from {@code 0.0} to {@code 1.0} (inclusive).
         *
         * @since 1.0.0
         */
        private final double y;


        /**
         * Constructs an instance representing a hot-spot at the specified location relative to the image.
         *
         * @param x the x-coordinate of the hot-spot, ranging from {@code 0.0} to {@code 1.0} (inclusive). An
         * x-coordinate of {@code 0.0} indicates the left side of the cursor, while an x-coordinate of {@code 1.0}
         * represents the right side.
         * @param y the y-coordinate of the hot-spot, ranging from {@code 0.0} to {@code 1.0} (inclusive). A
         * y-coordinate of {@code 0.0} indicates the top edge of the cursor, while a y-coordinate of {@code 1.0}
         * represents the bottom edge.
         * @since 1.0.0
         */
        private CursorHotspot(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        /**
         * Returns the x-coordinate of the hot-spot relative to the left side of the cursor image. The x-coordinate
         * ranges between {@code 0.0} on the left and {@code 1.0} on the right. 
         *
         * @return an x-value of the hot-spot between {@code 0.0} and {@code 1.0}.
         * @since 1.0.0
         */
        public double getX()
        {
            return x;
        }

        /**
         * Returns the y-coordinate of the hot-spot relative to the top edge of the cursor image. The y-coordinate
         * ranges between {@code 0.0} at the top and {@code 1.0} at the bottom. 
         *
         * @return an y-value of the hot-spot between {@code 0.0} and {@code 1.0}.
         * @since 1.0.0
         */
        public double getY()
        {
            return y;
        }
    }

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
     * @param deleteOnExit specifies whether or not the file should be deleted when the application terminates.
     * @return a {@code File} referfencing the newly created copy of the resource.
     * @throws IOException if the temporary file can not be created or the resource can not be copied into it.
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
        return getResourceAsFile(resourceName, file, deleteOnExit);
    }

    /**
     * Copies a named resource into the specified file on the file system. The file object is returned. This method is
     * useful when random access or marking within the resource stream are required and a specific path is desired. In
     * such a case (as with audio files), obtaining the resource as a stream from the default class loader is not
     * sufficient.
     *
     * @param resourceName the name of the resource to copy.
     * @param file the file to copy to. If the file exists, it will be overwritten.
     * @param deleteOnExit specifies whether or not the file should be deleted when the application terminates. If
     * {@code true}, {@link File#deleteOnExit()} will be invoked on the file. This operation can not be undone. Note
     * that if the file is slated for deletion on input and this flag is {@code false}, it will have no effect.
     * @return a {@code File} referencing the newly created copy of the resource.
     * @throws IOException if the temporary file can not be created or the resource can not be copied into it.
     * @since 1.1.0
     */
    public static File getResourceAsFile(String resourceName, File file, boolean deleteOnExit) throws IOException
    {
        if(deleteOnExit)
            file.deleteOnExit();
        copyResourceToFile(resourceName, file);
        return file;
    }

    /**
     * Copies a named resource into the specified file on the file system. The file object is returned. This method is
     * useful when random access or marking within the resource stream are required and a specific path is desired. In
     * such a case (as with audio files), obtaining the resource as a stream from the default class loader is not
     * sufficient.
     *
     * @param resourceName the name of the resource to copy.
     * @param fileName the name of the file to copy to. If the file exists, it will be overwritten.
     * @param deleteOnExit specifies whether or not the file should be deleted when the application terminates. If
     * {@code true}, {@link File#deleteOnExit()} will be invoked on the file.
     * @return a {@code File} referencing the newly created copy of the resource.
     * @throws IOException if the temporary file can not be created or the resource can not be copied into it.
     * @since 1.1.0
     */
    public static File getResourceAsFile(String resourceName, String fileName, boolean deleteOnExit) throws IOException
    {
        return getResourceAsFile(resourceName, new File(fileName), deleteOnExit);
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
        try(InputStream input = ClassLoader.getSystemResourceAsStream(resourceName)) {
            if(input == null)
                throw new FileNotFoundException(resourceName);
            FileUtilities.copyStreamToFile(input, file);
        }
    }

    /**
     * Extracts a native dynamically linked library from a resource and loads it
     * into memory. This method is particularly useful for loading .so and .dll
     * modules stored within a JAR file.
     *
     * @param parent the root path of the resource, minus the file name.
     * @param baseName the base name of the library. Any extensions will be
     * interpreted by the local platform as necessary.
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
     * Loads an image from a resource. To load the image as an {@code Icon}, use the {@link #loadIcon(java.lang.String)
     * loadIcon()} method.
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
        if(PREFER_TOOLKIT) {
            return Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource(resourceName));
        } else {
            try(InputStream input = ClassLoader.getSystemResourceAsStream(resourceName)) {
                return (input == null) ? null : ImageIO.read(input);
            }
        }
    }

    /**
     * Loads an icon from a resource. This method can load animated GIFs in addition
     * to regular icons. It returns {@code null} instead of throwing an exception.
     *
     * @param resourceName the name of the resource to load.
     * @return the icon named by the specified resource, or {@code null} if the resource could not be found or loaded.
     * @since 1.0.2
     */
    public static Icon loadIcon(String resourceName)
    {
        URL url = ClassLoader.getSystemResource(resourceName);
        return (url == null) ? null : new ImageIcon(url);
    }

    /**
     * Loads a cursor from a resource. The hot-spot is set to a selected predefined location.
     *
     * @param resourceName the name of an image resource to load.
     * @param hotspot the location of the hot-spot, as a predefined value with coordinates relative to the image.
     * @return the cursor named by the specified resource and hot-spot, or {@code null} if the image could not be found.
     * @throws IOException if the image of the cursor could not be loaded.
     * @see #loadCursor(String, double, double)
     * @since 1.1.0
     */
    public static Cursor loadCursor(String resourceName, CursorHotspot hotspot) throws IOException
    {
        return loadCursor(resourceName, hotspot.getX(), hotspot.getY());
    }

    /**
     * Loads a cursor from a resource. The hot-spot is set to the specified location. Hot-spot coordinates are
     * specified relative to the image. A hot-spot of {@code (0.0, 0.0)} corresponds to the upper-left corner of the
     * loaded image. A hot-spot of {@code (1.0, 1.0)} corresponds to the lower-right corner of the image, regardless of
     * the size of the actual image.
     *
     * @param resourceName the name of an image resource to load.
     * @param hotspotX the x-coordinate of the hot-spot. This number must be between {@code 0.0} and {@code 1.0}, or an
     * {@code IndexOutOfBoundsException} will be thrown.
     * @param hotspotY the y-coordinate of the hot-spot. This number must be between {@code 0.0} and {@code 1.0}, or an
     * {@code IndexOutOfBoundsException} will be thrown.
     * @return the cursor named by the specified resource and hot-spot, or {@code null} if the image could not be found.
     * @throws IOException if the image of the cursor could not be loaded.
     * @throws IndexOutOfBoundsException if either of the the hot-spot coordinates does not round to a pixel that is
     * not within the bounds of the cursor image.
     * @since 1.1.0
     */
    public static Cursor loadCursor(String resourceName, double hotspotX, double hotspotY) throws IOException
    {
        Image image = loadImage(resourceName);
        if(image == null)
            return null;
        Dimension optimalSize = Toolkit.getDefaultToolkit().getBestCursorSize(image.getWidth(null),
                                                                              image.getHeight(null));
        int x = (int)((double)optimalSize.width  * hotspotX - 0.5);
        int y = (int)((double)optimalSize.height * hotspotY - 0.5);
        Point hotspot = new Point(x, y);
        return Toolkit.getDefaultToolkit().createCustomCursor(image, hotspot, resourceName);
    }

    /**
     * Loads a cursor from a resource. The hot-spot is set to the specified location. Hot-spot coordinates are
     * specified relative to the image.
     *
     * @param resourceName the name of an image resource to load.
     * @param hotspot the desired location of the hot-spot, relative to the image. The upper-left of the image is
     * {@code (0.0, 0.0)}, while the lower-right corner is {@code (1.0, 1.0)}.
     * @return the cursor named by the specified resource and hot-spot, or {@code null} if the image could not be found.
     * @throws IOException if the image of the cursor could not be loaded.
     * @throws IndexOutOfBoundsException if either of the the hot-spot coordinates does not round to a pixel that is
     * not within the bounds of the cursor image.
     * @see #loadCursor(String, double, double)
     * @since 1.1.0
     */
    public static Cursor loadCursor(String resourceName, Point2D hotspot) throws IOException
    {
        return loadCursor(resourceName, hotspot.getX(), hotspot.getY());
    }

    /**
     * Loads a cursor from a resource. The hot-spot is set to the specified location in pixel coordinates. The hot-spot
     * location can be optionally scaled to match the transformation of the image into the best cursor size. If scaling
     * is enabled, the hot-spot coordinates are interpreted as relative to the original image, before it is scaled to
     * the cursor size. The hot-spot is scaled to preserve its location in the image as much as possible. If scaling is
     * not enabled, the hot-spot is not modified in any way.
     *
     * @param resourceName the name of an image resource to load.
     * @param hotspotX the x-coordinate of the desired location of the hot-spot, in pixels. 
     * @param scale if {@code true}, the hot-spot is scaled along with the image as much as possible. If {@code false},
     * the hot-spot coordinates are interpreted relative to the output image and no scaling is done.
     * @return the cursor named by the specified resource and hot-spot, or {@code null} if the image could not be found.
     * @throws IOException if the image of the cursor could not be loaded.
     * @throws IndexOutOfBoundsException if the hot-spot is outside the cursor image.
     * @see Toolkit#getBestCursorSize(int, int)
     * @since 1.1.0
     */
    public static Cursor loadCursor(String resourceName, int hotspotX, int hotspotY, boolean scale) throws IOException
    {
        return loadCursor(resourceName, new Point(hotspotX, hotspotY), scale);
    }

    /**
     * Loads a cursor from a resource. The hot-spot is set to the specified location in pixel coordinates. The hot-spot
     * location can be optionally scaled to match the transformation of the image into the best cursor size.
     *
     * @param resourceName the name of an image resource to load.
     * @param hotspot the desired location of the hot-spot, in pixels. If scaling is enabled, this point is interpreted
     * as the coordinates in the original image, before it is scaled to the cursor size. The hot-spot is scaled to
     * preserve its location in the image as much as possible. If scaling is not enabled, this point is interpreted as
     * the coordinates in the output image after it has been scaled. This point will contain the actual coordinates of
     * the hot-spot within the image if this method terminates successfully.
     * @param scale if {@code true}, the hot-spot is scaled along with the image as much as possible. If {@code false},
     * the hot-spot coordinates are interpreted relative to the output image and no scaling is done.
     * @return the cursor named by the specified resource and hot-spot, or {@code null} if the image could not be found.
     * @throws IOException if the image of the cursor could not be loaded.
     * @throws IndexOutOfBoundsException if the hot-spot is outside the cursor image. 
     * @see Toolkit#getBestCursorSize(int, int)
     * @since 1.1.0
     */
    public static Cursor loadCursor(String resourceName, Point hotspot, boolean scale) throws IOException
    {
        Image image = loadImage(resourceName);
        if(image == null)
            return null;
        Dimension optimalSize = Toolkit.getDefaultToolkit().getBestCursorSize(image.getWidth(null),
                                                                              image.getHeight(null));
        if(scale) {
            // actualPos = initalPos / originalSize * optimalSize
            hotspot.x = (int)(((float)hotspot.x / (float)image.getWidth(null)) * optimalSize.width);
            hotspot.y = (int)(((float)hotspot.y / (float)image.getHeight(null)) * optimalSize.height);
        }
        return Toolkit.getDefaultToolkit().createCustomCursor(image, hotspot, resourceName);
    }
}
