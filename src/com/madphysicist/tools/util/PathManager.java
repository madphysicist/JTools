/*
 * PathManager.java (Class: com.madphysicist.tools.util.PathManager)
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
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Manages the tasks of path construction and lookup. Lookup is done in a linear fashion according to the order in which
 * items are added. For a concrete implementation pertaining to file system paths, see {@link
 * com.madphysicist.tools.io.FilePathManager}.
 *
 * @param <T> the type of element in the path.
 * @param <U> the type of object obtained when an element is appended to the path.
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 03 Dec 2012 - J. Fox-Rabinovitz - Created
 * @since 1.0.1
 */
public abstract class PathManager<T, U> implements Serializable, Iterable<T>
{
	private static final long serialVersionUID = 10001L;

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

    public PathManager(Collection<T> initialPath)
    {
        this.path = new ArrayList<>();
        if(initialPath != null) {
            addElements(initialPath);
        }
    }

    public void insertElement(T element, int index)
    {
        if(element == null) {
            throw new NullPointerException("adding null element");
        }
        path.add(index, element);
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

    public void removeElement(int index)
    {
        path.remove(index);
    }

    public void removeElement(T element)
    {
        if(element == null) {
            throw new NullPointerException("removing null element");
        }
        int index = path.indexOf(element);
        if(index >= 0) {
            removeElement(index);
        }
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

    public T getElement(int index)
    {
        return path.get(index);
    }

    public List<T> getElements()
    {
        return Collections.unmodifiableList(path);
    }

    public int indexOf(T element)
    {
        return path.indexOf(element);
    }

    public int size()
    {
        return path.size();
    }

    public boolean isEmpty()
    {
        return path.isEmpty();
    }

    public boolean moveUp(T element)
    {
        return move(element, -1);
    }

    public boolean moveUp(int index)
    {
        return move(index, -1);
    }

    public boolean moveDown(T element)
    {
        return move(element, +1);
    }

    public boolean moveDown(int index)
    {
        return move(index, +1);
    }

    public boolean move(T element, int delta)
    {
        int index = indexOf(element);
        if(index >= 0) {
            return move(index, delta);
        }
        return false;
    }

    public boolean move(int index, int delta)
    {
        int newIndex = index + delta;

        if(newIndex < 0) {
            newIndex = 0;
        } else if(newIndex >= path.size()) {
            newIndex = path.size() - 1;
        }

        if(newIndex == index) {
            return false;
        }

        if(Math.abs(delta) <= 1) {
            // special case only moving by 1 element: swap
            T element = path.get(index);
            path.set(index, path.get(newIndex));
            path.set(newIndex, element);
        } else {
            T element = path.remove(index);
            path.add(newIndex, element);
        }

        return true;
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
        T element = whichElement(item);
        return (element == null) ? null : combine(element, item);
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

    public String getElementString(int index)
    {
        return getElementString(path.get(index));
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
