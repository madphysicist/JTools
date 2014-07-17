/*
 * SelfIterator.java
 *
 * Mad Physicist JTools Project
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

/**
 * <p>
 * A single-element iterator over a single object. This class is a much simpler version of doing something like {@code
 * Arrays.asList(new Object[] {object}).iterator()}. This iterator also provides a {@link #reset()} method that allows
 * it to be run multiple times.
 * </p>
 * <p>
 * This class does not support the {@link #remove()} method.
 * </p>
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 17 Jul 2014 - J. Fox-Rabinovitz - Initial coding
 * @since 1.0.3
 */
public class SelfIterator<E> implements Iterator<E>
{
    /**
     * The object to iterate over.
     *
     * @since 1.0.0
     */
    private final E object;

    /**
     * A flag indicating whether or not this iterator has a next element. This flag is set to {@code true} in the
     * {@linkplain #SelfIterator(Object) constructor} and {@code false} in the {@link #next()} method.
     *
     * @since 1.0.0
     */
    private boolean on;

    /**
     * Constructs a new iterator over the specified object.
     *
     * @param object the object to iterate over.
     * @since 1.0.0
     */
    public SelfIterator(E object)
    {
        this.object = object;
        this.on = true;
    }

    /**
     * Checks if the object has been iterated over.
     *
     * @return {@code true} as long as {@link #next()} has not been called.
     * @since 1.0.0
     */
    @Override public boolean hasNext()
    {
        return on;
    }

    /**
     * Returns the object being iterated over if it has not already been returned.
     *
     * @return the object which this iterator encapsulates.
     * @throws IllegalStateException if this method was called before and {@link #reset()} was not called.
     * @since 1.0.0 
     */
    @Override public E next()
    {
        if(this.on) {
            this.on = false;
            return object;
        }
        throw new IllegalStateException("No more elements in SelfIterator");
    }

    /**
     * An unsupported operation required by the {@code Iterator} interface. This method always throws an exception.
     *
     * @throws UnsupportedOperationException always.
     * @since 1.0.0
     */
    @Override public void remove()
    {
        throw new UnsupportedOperationException("Deleting from SelfIterator");
    }

    /**
     * Resets this iterator. Further calls to {@link #hasNext()} will return {@code true} and {@link #next()} can be
     * invoked after this method without throwing an exception. If the iterator is already active or has been reset,
     * this method has no effect.
     *
     * @since 1.0.0
     */
    public void reset()
    {
        this.on = true;
    }
}
