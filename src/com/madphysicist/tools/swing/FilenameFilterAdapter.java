/*
 * FilenameFilterAdapter.java (Class: com.madphysicist.tools.swing.FilenameFilterAdapter)
 *
 * Mad Physicist JTools Project (Swing Utilities)
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
package com.madphysicist.tools.swing;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A class for converting {@link java.io.FilenameFilter} into {@link
 * javax.swing.filechooser.FileFilter}.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 28 May 2013
 * @since 1.0.0
 */
public class FilenameFilterAdapter extends javax.swing.filechooser.FileFilter
{
    /**
     * The filter which provides the underlying decision-making for this
     * adapter.
     *
     * @since 1.0.0
     */
    private final FilenameFilter base;

    /**
     * The description of this adapter.
     *
     * @since 1.0.0
     */
    private final String description;

    /**
     * Constructs an implementation of {@link
     * javax.swing.filechooser.FileFilter} out of the specified {@link
     * java.io.FileFilter} and description string.
     *
     * @param filter the filter to convert.
     * @param description a description of the filter.
     * @since 1.0.0
     */
    public FilenameFilterAdapter(FilenameFilter filter, String description)
    {
        this.base = filter;
        this.description = description;
    }

    /**
     * {@inheritDoc} This method delegates the the base filter from which this
     * class is constructed.
     *
     * @param pathname {@inheritDoc}
     * @return {@inheritDoc}
     * @since 1.0.0
     */
    @Override public boolean accept(File pathname)
    {
        return base.accept(pathname.getParentFile(), pathname.getName());
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 1.0.0
     */
    @Override public String getDescription()
    {
        return description;
    }
}
