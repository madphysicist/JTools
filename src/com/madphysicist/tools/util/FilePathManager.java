/*
 * FilePathManager.java
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Manages the a file path and associated lookup tasks. This class is very
 * lenient with input types. Path elements must always be {@code File}s, but
 * checking for any non-{@code File} type is done by converting it to a {@code
 * String} first.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 03 Dec 2012 - J. Fox-Rabinovitz - Created
 * @since 1.0.1
 */
public class FilePathManager extends PathManager<File, File>
{
    /**
     * The version ID for serialization.
     *
     * @serial Increment the least significant three digits when compatibility
     * is not compromised by a structural change (e.g. adding a new field with
     * a sensible default value), and the upper digits when the change makes
     * serialized versions of of the class incompatible with previous releases.
     * @since 1.0.0
     */
    private static final long serialVersionUID = 1000L;

    public FilePathManager()
    {
        super();
    }

    public FilePathManager(String initialPath)
    {
        this(initialPath, "[:;]");
    }

    public FilePathManager(String initialPath, String splitRegex)
    {
        super(makeFiles(initialPath.split(splitRegex)));
    }

    public FilePathManager(String[] initialPath)
    {
        super(makeFiles(initialPath));
    }

    public FilePathManager(File[] initialPath)
    {
        super(makeFiles(initialPath));
    }

    public FilePathManager(Collection<?> initialPath)
    {
        super(makeFiles(initialPath));
    }

    public void addElement(String element)
    {
        addElement(new File(element));
    }

    public void addElements(String[] elements)
    {
        addElements(makeFiles(elements));
    }

    public void removeElement(String element)
    {
        removeElement(new File(element));
    }

    public void removeElements(String[] elements)
    {
        removeElements(makeFiles(elements));
    }

    public int indexOf(String element)
    {
        return indexOf(new File(element));
    }

    public boolean contains(String element)
    {
        return contains(new File(element));
    }

    @Override public boolean contains(File element, Object item)
    {
        return combine(element, item).exists();
    }

    @Override public File combine(File element, Object item)
    {
        return new File(element, item.toString());
    }

    @Override public String getElementString(File element)
    {
        try {
            return element.getCanonicalPath();
        } catch(IOException ioe) {
            return element.getAbsolutePath();
        }
    }

    private static List<File> makeFiles(String[] names)
    {
        return makeFiles(Arrays.asList(names));
    }

    private static List<File> makeFiles(File[] names)
    {
        return makeFiles(Arrays.asList(names));
    }

    private static List<File> makeFiles(Collection<?> names)
    {
        List<File> files = new ArrayList<>(names.size());
        for(Object name : names) {
            File file = null;
            if(name instanceof File) {
                file = (File)name;
            } else {
                file = new File(name.toString());
            }
            files.add(file);
        }
        return files;
    }

    public static FilePathManager getEnvironmentPath()
    {
        return getEnvironmentPath("PATH");
    }

    public static FilePathManager getEnvironmentPath(String variableName)
    {
        String value = System.getenv(variableName);
        if(value == null) {
            return new FilePathManager();
        }
        return new FilePathManager(value, File.pathSeparator);
    }

    public static FilePathManager getPropertyPath(String propertyName)
    {
        String value = System.getProperty(propertyName);
        if(value == null) {
            return new FilePathManager();
        }
        return new FilePathManager(value);
    }
}
