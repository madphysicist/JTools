/*
 * CombinedIterator.java (Class: com.madphysicist.tools.util.CombinedIterator)
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Chains the {@code Iterators} of multiple {@code Iterables} together. The
 * remove operation of this iterator is defined by the iterators of the
 * underlying {@code Iterables}. Likewise, the response to modification of the
 * underlying {@code Iterable}s depends on how their respective {@code
 * Iterator}s work. Check the documentation of the {@code iterator()} method of
 * each respective element for details.
 *
 * @param <T> the type of object to be iterated over.
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 19 Nov 2012
 * @since 1.0.0.0
 */
public class CombinedIterator<T> implements Iterator<T>
{
    /**
     * The objects to be iterated over. Objects are iterated over in the order
     * that they appear in this array.
     * @since 1.0.0.0
     */
    private Iterable<T>[] list;

    /**
     * The native iterator of the object currently being iterated over. The
     * {@link #hasNext()}, {@link #next()} and {@link #remove()} methods of this
     * iterator will delegate to the current iterator.
     * @since 1.0.0.0
     */
    private Iterator<T> currentIterator;

    /**
     * The index in {@link #list} of the object currently being iterated over.
     * @since 1.0.0.0
     */
    private int index;

    /**
     * Initializes a {@code CombinedIterator} over the specified list of
     * objects. Objects will be iterated over in sequence, in the order that
     * they appear in the list.
     * @param list a list of {@code Iterable} objects. The array iself may be
     * {@code null}. Any {@code null} or empty elements it contains, will be
     * skipped without throwing an exception.
     * @since 1.0.0.0
     */
    @SuppressWarnings("unchecked")
    public CombinedIterator(Iterable<T>... list)
    {
        this.list = list;
        // initialize the first iterator, if there is one
        this.index = -1;
        setNextIterator();
    }

    /**
     * {@inheritDoc}
     *
     * This method delegates to the {@code hasNext()} method of the iterator of
     * the current {@code Iterable}. This method will return {@code true} even
     * if the current iterator runs out, as long as there are more objects to
     * iterate over.
     *
     * @return {@inheritDoc}
     * @since 1.0.0.0
     */
    @Override public boolean hasNext()
    {
        // if there is a workable iterator, use it
        if(currentIterator != null && currentIterator.hasNext())
            return true;
        // otherwise, try to set a new one
        return setNextIterator();
    }

    /**
     * {@inheritDoc}
     *
     * This method delegates to the {@code next()} method of the iterator of the
     * the current {@code Iterable}. This method will return a valid object even
     * if the current iterator runs out, as long as there are more objects to
     * iterate over.
     *
     * @return {@inheritDoc}
     * @throws NoSuchElementException when all the {@code Iterables} have run
     * out.
     * @since 1.0.0.0
     */
    @Override public T next() throws NoSuchElementException
    {
        // if there is no possible next element, throw an exception
        if(!hasNext())
            throw new NoSuchElementException();
        // otherwise, delegate
        return currentIterator.next();
    }

    /**
     * {@inheritDoc}
     *
     * The behavior of this method depends entirely on the underlying iterator.
     *
     * @throws UnsupportedOperationException if the current {@code Iterable}'s
     * {@code Iterator} does not support removal.
     * @throws IllegalStateException if there are no more {@code Iterables} left
     * in the sequence.
     * @since 1.0.0.0
     */
    @Override public void remove() throws UnsupportedOperationException, IllegalStateException
    {
        if(currentIterator == null)
            throw new IllegalStateException();
        currentIterator.remove();
    }

    /**
     * Finds the next element in {@link #list} that is not {@code null} and has
     * a non-empty {@code Iterator}. Empty {@code null} {@code Iterables}, as
     * well as those with empty {@code Iterators} are skipped. The {@link
     * #currentIterator} field is set to {@code null} if an iterator could not
     * be found.
     *
     * @return {@code true} if a valid iterator was found, {@code false} if
     * there are no more elements left to iterate over in any of the {@code
     * Iterables}.
     * @since 1.0.0.0
     */
    private boolean setNextIterator()
    {
        // a null list is OK
        if(list != null) {
            for(index++; index < list.length; index++) {
                // skip null elements
                if(list[index] == null)
                    continue;
                currentIterator = list[index].iterator();
                // skip empty iterators
                if(currentIterator.hasNext())
                    return true;
            }
        }
        currentIterator = null;
        return false;
    }
}
