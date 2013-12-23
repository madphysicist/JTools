/*
 * PathManager.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Manages the tasks of path construction and lookup. Lookup is done in a linear
 * fashion according to the order in which items are added. For a concrete
 * implementation pertaining to file system paths, see {@link FilePathManager}.
 *
 * @param <T> the type of element in the path.
 * @param <U> the type of object obtained when an element is appended to the
 * path.
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 03 Dec 2012 - J. Fox-Rabinovitz - Created
 * @since 1.0.1
 */
public abstract class PathManager<T, U> implements Serializable, Iterable<T>
{
    /**
     * The list of path elements. The order of the elements is significant for
     * how items will be found.
     *
     * @serial
     * @since 1.0.0
     */
    private final List<T> path;

    public PathManager()
    {
        this.path = new ArrayList<>();
    }

    public PathManager(T[] initialPath)
    {
        this(Arrays.asList(initialPath));
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public PathManager(Collection<T> initialPath)
    {
        this.path = new ArrayList<>();
        if(initialPath != null) {
            addElements(initialPath);
        }
    }

    public void addElement(T element)
    {
        if(element == null) {
            throw new NullPointerException("adding null element");
        }
        path.add(element);
    }

    public void addElements(T[] elements)
    {
        addElements(Arrays.asList(elements));
    }

    public void addElements(Collection<T> elements)
    {
        for(T element : elements) {
            addElement(element);
        }
    }

    public void removeElement(T element)
    {
        if(element == null) {
            throw new NullPointerException("removing null element");
        }
        path.remove(element);
    }

    public void removeElements(T[] elements)
    {
        removeElements(Arrays.asList(elements));
    }

    public void removeElements(Collection<T> elements)
    {
        for(T element : elements) {
            removeElement(element);
        }
    }

    public int indexOf(T element)
    {
        return path.indexOf(element);
    }

    public boolean contains(T element)
    {
        return path.contains(element);
    }

    public boolean exists(Object item)
    {
        return whichElement(item) != null;
    }

    public T whichElement(Object item)
    {
        for(T element : path) {
            if(contains(element, item)) {
                return element;
            }
        }
        return null;
    }

    public U which(Object item)
    {
        return combine(whichElement(item), item);
    }

    public String toString()
    {
        if(path.isEmpty()) {
            return super.toString() + ": <Empty Path>";
        }

        StringBuilder sb = new StringBuilder(super.toString());
        sb.append('\n');
        for(T element : path) {
            sb.append(getElementString(element)).append('\n');
        }
        return sb.toString();
    }

    public String getElementString(T element)
    {
        return element.toString();
    }

    @Override public ListIterator<T> iterator()
    {
        return path.listIterator();
    }

    /**
     * Checks if the specified manager is equal to this one. The managers are
     * considered equal if an only if they are of the exact same class and
     * contain the same elements in the same order. A return value of {@code
     * true} can be used as an indicator of the fact that this object and the
     * one being compared to are of the same class.
     *
     * @param o the object to compare this one to.
     * @return {@code true} if both objects have the exact same class and the
     * same elements in the same order.
     * @since 1.0.0
     */
    @Override public boolean equals(Object o)
    {
        if(this.getClass() == o.getClass()) {
            return this.path.equals(((PathManager<?, ?>)o).path);
        }
        return false;
    }

    @Override public int hashCode()
    {
        return HashUtilities.hashCode(path);
    }

    public abstract boolean contains(T element, Object item);

    public abstract U combine(T element, Object item);
}
