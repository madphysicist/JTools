/*
 * FileUtilities.java (Class: com.madphysicist.tools.util.FileUtilities)
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * A utility class for manipulating system files and their names. This class can
 * not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 16 Nov 2013 - J. Fox-Rabinovitz - Created
 * @since 1.0
 */
public class FileUtilities
{
    /**
     * The approximate size of a disk block in bytes. This number is intended to
     * be nearly optimal for copying files across multiple platforms. It may be
     * advisable to compute this constant for each device being copied to if
     * such a method should become readily available.
     * <p>
     * This number is set as a reasonable upper bound on the block size across
     * most platforms. The idea being that it is better to read multiple blocks
     * into a single buffer than to have to perform multiple reads for a single
     * large block if the buffer is too small. Similar logic applies to writing
     * the buffer back to disk.
     *
     * @since 1.0.0
     */
    private static final int DISK_BLOCK = 8192;

    /**
     * A private constructor to prevent instantiation.
     *
     * @since 1.0.0
     */
    private FileUtilities() {}

    /**
     * Checks if the specified file exists. If it does not, the user is asked to
     * select a replacement from a file chooser dialog.
     *
     * @param filename the name of the file to check.
     * @param filter the filter to apply in the dialog if the file does not exist. May be {@code null}.
     * @return the original file if it exists, or the replacement file if another one had to be selected.
     * @throws FileNotFoundException if the user refuses to select a file, or selects a non-existent one.
     * @since 1.0.0
     */
    public static File promptFile(String filename, FileFilter filter) throws FileNotFoundException
    {
        return promptFile(filename, filter, null);
    }

    /**
     * Checks if the specified file exists. If it does not, the user is asked to
     * select a replacement from a file chooser dialog.
     *
     * @param filename the name of the file to check.
     * @param filter the filter to apply in the dialog if the file does not exist. May be {@code null}.
     * @param title the title to set fot the dialog if the file does not exist. May be {@code null}.
     * @return the original file if it exists, or the replacement file if another one had to be selected.
     * @throws FileNotFoundException if the user refuses to select a file, or selects a non-existent one.
     * @since 1.0.0
     */
    public static File promptFile(String filename, FileFilter filter, String title) throws FileNotFoundException
    {
        return promptFile((filename == null) ? null : new File(filename), filter, title);
    }

    /**
     * Checks if the specified file exists. If it does not, the user is asked to
     * select a replacement from a file chooser dialog.
     *
     * @param file the file to check.
     * @param filter the filter to apply in the dialog if the file does not exist. May be {@code null}.
     * @param title the title to set for the dialog if the file does not exist. May be {@code null}.
     * @return the original file if it exists, or the replacement file if another one had to be selected.
     * @throws FileNotFoundException if the user refuses to select a file, or selects a non-existent one.
     * @since 1.0.0
     */
    public static File promptFile(File file, FileFilter filter, String title) throws FileNotFoundException
    {
        if(file == null || !file.exists()) {
            File start = file.getParentFile();
            if(!start.exists()) {
                start = new File(".");
            }
            JFileChooser chooser = new JFileChooser(start);

            if(title != null) {
                chooser.setDialogTitle(title);
            }
            if(filter != null) {
                chooser.addChoosableFileFilter(filter);
            }
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setAcceptAllFileFilterUsed(true);

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
            } else {
                file = null;
            }
            if(file == null || !file.exists()) {
                throw new FileNotFoundException((file == null) ? "" : file.getAbsolutePath());
            }
        }
        
        return file;
    }

    /**
     * Copies the contents of one stream to another. Useful for copying files or
     * resources. To copy files, use the {@link #copyFile(java.io.File,
     * java.io.File) copyFile()} method.
     *
     * @param from the stream from which to copy.
     * @param to the stream to copy to.
     * @throws IOException if an error occurrs while reading or writing the
     * streams.
     * @since 1.0.0
     */
    public static void copyStream(InputStream from, OutputStream to) throws IOException
    {
        byte[] buffer = new byte[DISK_BLOCK];
        int nRead;
        while((nRead = from.read(buffer)) != -1) {
            to.write(buffer, 0, nRead);
        }
    }

    /**
     * Copies the contents of one file to another.
     *
     * @param from the file from which to copy.
     * @param to the file to copy to.
     * @throws IOException if an error occurrs while reading or writing the
     * files.
     * @since 1.0.0
     */
    public static void copyFile(File from, File to) throws IOException
    {
        try (InputStream input = new FileInputStream(from);
             OutputStream output = new FileOutputStream(to)) {
            FileUtilities.copyStream(input, output);
        }
    }

    /**
     * Copies the contents of an arbitrary stream into a file. This is useful
     * for creating files from various types of resources.
     *
     * @param from the stream from which to copy.
     * @param to the file to copy to.
     * @throws IOException if an error occurrs while reading the stream or
     * writing to the file.
     * @since 1.0.0
     */
    public static void copyStreamToFile(InputStream from, File to) throws IOException
    {
        try (OutputStream output = new FileOutputStream(to)) {
            FileUtilities.copyStream(from, output);
        }
    }

    /**
     * Copies the contents of a file into an arbitrary stream. This can be
     * useful for sending files through various types of streams.
     *
     * @param from the file to copy.
     * @param to the stream to copy to.
     * @throws IOException if an error occurrs while reading the file or
     * writing to the stream.
     * @since 1.0.0
     */
    public static void copyFileToStream(File from, OutputStream to) throws IOException
    {
        try (InputStream input = new FileInputStream(from)) {
            FileUtilities.copyStream(input, to);
        }
    }
}
